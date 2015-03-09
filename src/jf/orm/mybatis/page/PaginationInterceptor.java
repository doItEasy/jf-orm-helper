package jf.orm.mybatis.page;



import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jf.orm.mybatis.DataBaseTypes;
import jf.orm.mybatis.page.oracle.OraclePaggingDialect;
import jf.orm.mybatis.page.postgre.PostgreSqlPaggingDialect;
import jf.util.ArrayUtils;


import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;




/**
 * 基于 MyBatis 的分页查询拦截器�?
 * 
 */
@Intercepts( { @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class PaginationInterceptor implements org.apache.ibatis.plugin.Interceptor {

    /** MappedStatement 索引位置 */
    static final int MAPPED_STATEMENT_INDEX = 0;

    /** 参数 索引位置 */
    static final int PARAMETER_INDEX = 1;

    /** RowBounds 分页信息 索引位置 */
    static final int ROWBOUNDS_INDEX = 2;

    /** ResultHandler 查询结果处理 索引位置 */
    static final int RESULT_HANDLER_INDEX = 3;

    private Properties properties;
    
	@Value(value = "${dbtype:}")
	private String dbtype;
    
    private static Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = paggingProcessIntercept(invocation);
        logger.debug("<== SQL excuting times: {} ms", System.currentTimeMillis() - start);
        return proceed;
    }

    public Object paggingProcessIntercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Object parameter = args[PARAMETER_INDEX];
        RowBounds rowBounds =(RowBounds) args[ROWBOUNDS_INDEX];

        if (rowBounds == null || rowBounds == RowBounds.DEFAULT
                || (rowBounds.getLimit() == RowBounds.NO_ROW_LIMIT && rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET)) {
            return invocation.proceed();
        }

        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();

        if (offset <= 0 || limit <= 0) {
            return invocation.proceed();
        }
        try {
        	PageInfo pageInfo = null;
            
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();
            PaggingDialect dialect = getPaggingDialect(dbtype);

            // 分页SQL
            String paggingSql = dialect.getPaggingSql(sql, offset, limit);
            BoundSql newPaggingBoundSql = new BoundSql(ms.getConfiguration(), paggingSql, boundSql
                    .getParameterMappings(), boundSql.getParameterObject());
            copyAdditionalParameters(boundSql, newPaggingBoundSql);
            MappedStatement newPaggingMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newPaggingBoundSql),false);

            if(rowBounds  instanceof PageInfo){
            	// 分页获取count
                try {
                    String countSql = dialect.getCountSql(sql);
                    BoundSql newCountBoundSql = new BoundSql(ms.getConfiguration(), countSql, boundSql
                            .getParameterMappings(), boundSql.getParameterObject());
                    copyAdditionalParameters(boundSql, newCountBoundSql);
                    MappedStatement newCountMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newCountBoundSql),true);
                    args[ROWBOUNDS_INDEX] = RowBounds.DEFAULT;
                    args[MAPPED_STATEMENT_INDEX] = newCountMs;

                    List coun = (List) invocation.proceed();
                    pageInfo = (PageInfo)rowBounds;
                    pageInfo.setTotalCount(Integer.parseInt(coun.get(0).toString()));
                    logger.debug("<== SQL total count is {}:",coun.get(0));
                }
                catch (Exception e) {
                    // Ignore 忽略, 不要因为获取TotalCount失败影响正常的查询执�?
                    logger.trace("<== SQL 分页发生异常:", e);
                }
            }
            

            args[ROWBOUNDS_INDEX] = RowBounds.DEFAULT;
            args[MAPPED_STATEMENT_INDEX] = newPaggingMs;

            Object object = invocation.proceed();
            args[ROWBOUNDS_INDEX] = pageInfo;
            return object;
        }
        catch (Exception e) {
            logger.error("<== SQL 分页发生异常:", e);
            args[MAPPED_STATEMENT_INDEX] = ms;
            args[PARAMETER_INDEX] = parameter;
            args[ROWBOUNDS_INDEX] = rowBounds;
            return invocation.proceed();
        }

    }

    /**
     * 拷贝BoundSql的additionalParameters属�?，该属�?为私有并且没有提供set方法
     * �?��拷贝时代码较复杂，可以参考org.apache.ibatis.executor.parameter.setParameters 
     * 这儿并没有拷贝到�?��的参数，但是用户传�?参数已经全部拷贝出来 �?
     * 
     * @param fromBoundSql
     * @param toBoundSql
     */
    private void copyAdditionalParameters(BoundSql fromBoundSql, BoundSql toBoundSql) {
        List<ParameterMapping> parameterMappings = fromBoundSql.getParameterMappings();
        Object value;
        ParameterMapping parameterMapping;
        String propertyName;
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    propertyName = parameterMapping.getProperty();
                    if (fromBoundSql.hasAdditionalParameter(propertyName)) {
                        value = fromBoundSql.getAdditionalParameter(propertyName);
                        toBoundSql.setAdditionalParameter(propertyName, value);
                    }
                }
            }
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource,boolean count) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.keyProperty(ArrayUtils.toString(ms.getKeyProperties()));
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        if(count){
        	 ResultMap.Builder rb =new ResultMap.Builder(ms.getConfiguration(),"count",Integer.class,new ArrayList(1));
             ResultMap rm=rb.build();
             List<ResultMap> resultMaps = new ArrayList<ResultMap>(1);
             resultMaps.add(rm);
             builder.resultMaps(resultMaps);
        }
        MappedStatement newMs = builder.build();
        return newMs;
    }

    
    private PaggingDialect getPaggingDialect(String dbType){
    	 PaggingDialect dialect = null;
         if (DataBaseTypes.ORACLE.equals(dbType)) {
             dialect = new OraclePaggingDialect();
         }
         else if (DataBaseTypes.POSTGRESQL.equals(dbType)) {
             dialect = new PostgreSqlPaggingDialect();
         }else{
        	 dialect = new PostgreSqlPaggingDialect();
         }
         return dialect;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    public static class BoundSqlSqlSource implements SqlSource {

        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }


	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

    
    
    
}
