package com.personal.springboot.gataway.service.base;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import com.personal.springboot.gataway.dao.BaseDao;
import com.personal.springboot.gataway.dao.base.BaseEntity;

public class BaseServiceImpl<T> implements BaseService<T>{

	@Resource(name = "redisTemplate_openapi")
	protected RedisTemplate<String, Object> redisTemplate;
	
	@Value("${redis.prefix}")
	private String rePrefix;	//环境的前缀	
	
	private BaseDao<T> clazzDao;
	
	protected boolean ignoreRedis = true;
	
	public BaseDao<T> getBaseDao() {
		return clazzDao;
	}

	public void setBaseDao(BaseDao<T> clazzDao) {
		this.clazzDao = clazzDao;
	}

	
	private String  getKey(T t){
		BaseEntity be=(BaseEntity)t;
		String key=rePrefix+be.getRedisKey();
		return key;
	}
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		T t=selectByPrimaryKey(id);
		int i=	clazzDao.deleteByPrimaryKey(id);
		
		if (!ignoreRedis&&i>0) {			
			redisTemplate.opsForHash().delete(t.getClass().getName(), getKey(t));
		}
		return i;
	}
	
	@Override
	public int insert(T record) {
		int result =clazzDao.insert(record);
		BaseEntity be=(BaseEntity)record;
		String key=rePrefix+be.getRedisKey();
		if (!ignoreRedis) {
			redisTemplate.opsForHash().put(record.getClass().getName(), key,record);
		}
		return result;
	}

	@Override
	public int insertSelective(T record) {
		int result=clazzDao.insertSelective(record);
		
		if (!ignoreRedis && result>0) {
			redisTemplate.opsForHash().put(record.getClass().getName(), getKey(record),record);
		}
		return result;
	}

	@Override
	public T selectByPrimaryKey(Integer id) {
		return (T) clazzDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		int result=clazzDao.updateByPrimaryKeySelective(record);
		if (!ignoreRedis && result>0 ) {
			redisTemplate.opsForHash().put(record.getClass().getName(), getKey(record),record);
		}	
		return result;
	}

	@Override
	public int updateByPrimaryKey(T record) {
		int result=clazzDao.updateByPrimaryKey(record);
		if (!ignoreRedis && result>0) {
			redisTemplate.opsForHash().put(record.getClass().getName(), getKey(record),record);
		}
		return result;
	}

	@Override
	public T selectOneByEntity(T obj) {		
		return (T) clazzDao.selectOneByEntity(obj);
	}
	
	@Override
	public List<T> selectByEntity(T obj) {
		return clazzDao.selectByEntity(obj);
	}

	@Override
	public List<T> selectOnlyDelFlagData(T obj) {
		return clazzDao.selectOnlyDelFlagData(obj);
	}

	@Override
	public List<T> selectWithOutDelFlagData(T obj) {	
		return clazzDao.selectWithOutDelFlagData(obj);
	}

	@Override
	public List<T> selectAll() {
		return clazzDao.selectAll();
	}

	@Override
	public int deleteToHisDataByKey(Integer id) {
		return deleteToHisDataByEntity(selectByPrimaryKey(id));
	}

	@Override
	public int deleteToHisDataByEntity(T obj) {
		int result=clazzDao.deleteToHisDataByEntity(obj);	
		if (!ignoreRedis && result>0) {
			redisTemplate.opsForHash().delete(obj.getClass().getName(), getKey(obj));
		}
		return result;
	}


}
