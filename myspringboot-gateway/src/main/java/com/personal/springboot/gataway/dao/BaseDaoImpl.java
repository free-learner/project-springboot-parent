package com.personal.springboot.gataway.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.personal.springboot.gataway.dao.base.BaseEntity;


/**
 * 继承SqlSessionDaoSupport由Spring管理SqlSession无需实现open/close/commit/rollback
 * 
 * @author liuliu 
 *
 * @param <T>
 */
public class BaseDaoImpl<T> extends SqlSessionDaoSupport implements BaseDao<T>{
	
	protected String mapperName ="";
	 
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}
    
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return getSqlSession().delete("com.crt.openapi.modules.common.domain.mapper."+mapperName+".deleteByPrimaryKey", id);
	}

	@Override
	public int insert(T record) {
		if(record instanceof BaseEntity){
			fillBaseEntityNullDateProperties((BaseEntity)record);
		}
		return getSqlSession().insert("com.crt.openapi.modules.common.domain.mapper."+mapperName+".insert", record);
	}

	@Override
	public int insertSelective(T record) {
		if(record instanceof BaseEntity){
			fillBaseEntityNullDateProperties((BaseEntity)record);
		}
		return getSqlSession().insert("com.crt.openapi.modules.common.domain.mapper."+mapperName+".insertSelective", record);
	}

	@Override
	public T selectByPrimaryKey(Integer id) {
		return getSqlSession().selectOne("com.crt.openapi.modules.common.domain.mapper."+mapperName+".selectByPrimaryKey", id);
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		return getSqlSession().update("com.crt.openapi.modules.common.domain.mapper."+mapperName+".updateByPrimaryKeySelective", record);
	}

	@Override
	public int updateByPrimaryKey(T record) {
		return getSqlSession().update("com.crt.openapi.modules.common.domain.mapper."+mapperName+".updateByPrimaryKey", record);
	}

	@Override
	public T selectOneByEntity(T obj) {
		return getSqlSession().selectOne("com.crt.openapi.modules.common.domain.mapper."+mapperName+".selectOneByEntity", obj);
	}

	@Override
	public List<T> selectByEntity(T obj) {
		return getSqlSession().selectList("com.crt.openapi.modules.common.domain.mapper."+mapperName+".selectByEntity", obj);
	}
	
	@Override
	public List<T> selectOnlyDelFlagData(T obj) {	
		return getSqlSession().selectList("com.crt.openapi.modules.common.domain.mapper."+mapperName+".selectOnlyDelFlagData", obj);
	}

	@Override
	public List<T> selectWithOutDelFlagData(T obj) {
		return getSqlSession().selectList("com.crt.openapi.modules.common.domain.mapper."+mapperName+".selectWithOutDelFlagData", obj);
	}

	@Override
	public List<T> selectAll() {
		return getSqlSession().selectList("com.crt.openapi.modules.common.domain.mapper."+mapperName+".selectAll");
	}
	
	@Override
	public int deleteToHisDataByKey(Integer id) {
		return getSqlSession().update("com.crt.openapi.modules.common.domain.mapper."+mapperName+".deleteToHisDataByKey",id);
	}

	@Override
	public int deleteToHisDataByEntity(T obj) {	
		return getSqlSession().update("com.crt.openapi.modules.common.domain.mapper."+mapperName+".deleteToHisDataByEntity",obj);
	}	
	
	
	/**
	 * insert的时候,填充基本属性空值信息
	 */
	@SuppressWarnings("unchecked")
	public <E extends BaseEntity> void fillBaseEntityNullDateProperties(E entity) {
		PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
		Map<String, Object> map;
		try {
			map = propertyUtils.describe(entity);
			Date currentDate = new Date();
			if (map.size() > 0) {
				for (String key : map.keySet()) {
					Class<?> pClazz = propertyUtils.getPropertyType(entity, key);
					if (Date.class.isAssignableFrom(pClazz)) {
						Object value = map.get(key);
						if (value == null || StringUtils.isBlank(value.toString())) {
							propertyUtils.setProperty(entity, key, currentDate);
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页参数信息
	 */
	protected RowBounds makeRowBounds(int pageIndex, int pageSize) {
		pageIndex = pageIndex < 1 ? 1 : pageIndex;
		//pageSize = (pageSize <= 0 || pageSize > 1000) ? 10 : pageSize;
		pageSize = (pageSize <= 0 ) ? 10 : pageSize;
		if (logger.isInfoEnabled()) {
			logger.info("分页参数为:【pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
		}
		return new RowBounds((pageIndex - 1) * pageSize, pageSize);
		// return new RowBounds((pageIndex - 1) * pageSize+1, pageSize);
	}


	
}