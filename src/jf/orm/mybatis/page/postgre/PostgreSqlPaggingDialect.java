package jf.orm.mybatis.page.postgre;



import jf.orm.mybatis.page.DefaultPaggingDialect;




/**
 * PostgreSql 数据库分页查询方�??
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class PostgreSqlPaggingDialect extends DefaultPaggingDialect {

    @Override
    public String getPaggingSql(String querySql, int pageNo, int pageSize) {
        int myPageNo = (pageNo > 0 ? pageNo : 1);
        int myPageSize = (pageSize > 0 ? pageSize : 10);
        int begin = (myPageNo - 1) * myPageSize;

        String sql = querySql.trim() + " OFFSET " + begin + " LIMIT " + myPageSize;
        return sql;
    }

    @Override
    public String getCountSql(String querySql) {
       // String endSql = querySql.substring(querySql.indexOf("FROM"));
        int i = querySql.toUpperCase().indexOf("ORDER");
        if (i != -1) {
        	querySql = querySql.substring(0,i);            
        }
        String countSql = "SELECT COUNT(1)  FROM (" + querySql+") t";
        return countSql;
    }

}
