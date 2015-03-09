package jf.orm.mybatis.support;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jf.orm.mybatis.page.PageInfo;





/**
 * <p>
 * MyBatis Dao 基类接口，动态代理Dao接口对某个对象（数据库表）的操作可直接集成该接口�?
 * </p>
 * <p>
 * 该基类接口提供了�?��基础的功能，比如CURD操作，分页查询�?
 * 子接口无�?��写这些方法，直接在MyBatis的mapper.xml实现即可�?
 * </p>
 * 
 * @author Allen.Hu / 2012-7-2
 * @since SkyMarket 1.0
 */
public interface IMyBatisGenericDao<T, PK extends Serializable> {

    void insert(T object);
    
    void delete(PK id);
    
    void update(T object);

    T get(PK id);
    
    int count(Object condition);
    
    List<T> queryAll();
    
    List<T> queryAll(PageInfo<?> pageInfo);
    
    List<T> queryAll(Object c,PageInfo<?> pageInfo);
    
    List<T> findBySql(String sql);
    
    List<T> findByValue(String name, Object value);
    
    List<T> findByLikeValue(String name, String value); 
    
    List<T> findByMap(Map<?, ?> map);
    
    boolean isNotUniqueByOr(T object, String tableName, String names);
    
    boolean isNotUniqueByAnd(T object, String tableName, String names);
    
}
