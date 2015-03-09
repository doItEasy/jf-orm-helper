package jf.orm.mybatis.support;




import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jf.orm.mybatis.page.PageInfo;

public abstract class MyBatisAbstractService <T extends Serializable, PK extends Serializable, D extends IMyBatisGenericDao<T, PK>> implements IMyBatisGenericService<T, PK>{

	@Transactional(readOnly = true)
	protected abstract D getDao();
	
	@Transactional
	public void insert(T object) {
		getDao().insert(object);
	}

	@Transactional
	public void delete(PK id) {
		getDao().delete(id);
	}
	
	@Transactional
	public void delete(List<PK> ids) {
		for(PK id : ids){
			getDao().delete(id);
		}
	}

	@Transactional
	public void update(T object) {
		getDao().update(object);
	}

	@Transactional(readOnly = true)
	public T get(PK id) {
		return getDao().get(id);
	}

	@Transactional(readOnly = true)
	public List<T> queryAll() {
		return getDao().queryAll();
	}

	@Transactional(readOnly = true)
	public List<T> queryAll(PageInfo<?> pageInfo) {
        //pageInfo.setTotalCount(count(null));
		return getDao().queryAll(pageInfo);
	}

    @Transactional(readOnly = true)
    public List<T> queryAll(Object condition, PageInfo<?> pageInfo) {
        //pageInfo.setTotalCount(count(condition));
        return getDao().queryAll(condition,pageInfo);
    }

	@Transactional(readOnly = true)
	public List<T> findBySql(String sql) {
		return getDao().findBySql(sql);
	}

	@Transactional(readOnly = true)
	public List<T> findByValue(String name, Object value) {
		return getDao().findByValue(name, value);
	}

	@Transactional(readOnly = true)
	public List<T> findByLikeValue(String name, String value) {
		return getDao().findByLikeValue(name, value);
	}

	@Transactional(readOnly = true)
	public List<T> findByMap(Map<?, ?> map) {
		return getDao().findByMap(map);
	}

	@Transactional(readOnly = true)
	public boolean isNotUniqueByOr(T object, String tableName, String names) {
		return getDao().isNotUniqueByOr(object, tableName, names);
	}

	@Transactional(readOnly = true)
	public boolean isNotUniqueByAnd(T object, String tableName, String names) {
		return getDao().isNotUniqueByAnd(object, tableName, names);
	}

    @Transactional(readOnly = true)
    public int count(Object condtion) {
        return getDao().count(condtion);
    }
}
