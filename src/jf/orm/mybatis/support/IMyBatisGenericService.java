package jf.orm.mybatis.support;


/*
 * 文件名称: IServiceSupport.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-7-2
 * 修改内容: 
 */

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jf.orm.mybatis.page.PageInfo;





/**
 * 业务层的基类接口�?
 * 该接口封装了�?��基础公共的处理方法，比如CURD，分页查询�?
 * 如果�?��添加其他业务处理方法，请在子接口中添加�?
 * 
 * @author Allen.Hu / 2012-7-2
 * @since SkyMarket 1.0
 */
public interface IMyBatisGenericService<T, PK extends Serializable> {

	/**
	 * 新增�?��记录�?
	 * 
	 * @param object
	 * @author Allen.Hu / 2012-7-16 
	 * @since SkyMarket 1.0
	 */
	void insert(T object);
    
    /**
     * 删除�?��记录�?
     * 
     * @param id
     * @author Allen.Hu / 2012-7-16 
     * @since SkyMarket 1.0
     */
    void delete(PK id);
    
    /**
     * 更新�?��记录�?
     * 
     * @param object
     * @author Allen.Hu / 2012-7-16 
     * @since SkyMarket 1.0
     */
    void update(T object);

    /**
     * 根据唯一标识获得该记录�?
     * 
     * @param id
     * @return
     * @author Allen.Hu / 2012-7-16 
     * @since SkyMarket 1.0
     */
    T get(PK id);
    
    /**
     * 查询�?��的记录�?
     * 
     * @return
     * @author Allen.Hu / 2012-7-16 
     * @since SkyMarket 1.0
     */
    List<T> queryAll();
    
    /**
     * 根据分页属�?查询分页�?��定的记录�?
     * 
     * @param pageInfo
     * @return
     * @author Allen.Hu / 2012-7-16 
     * @since SkyMarket 1.0
     */
    List<T> queryAll(PageInfo<?> pageInfo);
    
    /**
     * 根据条件及分页查询指定的记录
     * 
     * @param condition
     * @param pageInfo
     * @return
     * @author Allen / 2012-9-11 
     */
    List<T> queryAll(Object condition, PageInfo<?> pageInfo);
    
    
    List<T> findBySql(String sql);
    
    List<T> findByValue(String name, Object value);
    
    List<T> findByLikeValue(String name, String value); 
    
    List<T> findByMap(Map<?, ?> map);
    
    boolean isNotUniqueByOr(T object, String tableName, String names);
    
    boolean isNotUniqueByAnd(T object, String tableName, String names);
    
}
