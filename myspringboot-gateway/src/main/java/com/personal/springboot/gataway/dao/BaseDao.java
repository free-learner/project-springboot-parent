package com.personal.springboot.gataway.dao;

import java.util.List;


/**
 * 常用增删改查接口基础类
 * 
 * @author liuliu
 *
 */
public interface BaseDao<T> {
	
	/**
	 * 按主键删除数据
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 按实体类所有字段插入数据
	 * @param record
	 * @return
	 */
	int insert(T record);

	/**
	 * 按实体类选择字段插入数据
	 * @param record
	 * @return
	 */
	int insertSelective(T record);

	/**
	 * 按主键查询，返回实体类
	 * @param id
	 * @return
	 */
	T selectByPrimaryKey(Integer id);

	/**
	 * 按主键update选着性字段
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(T record);

	/**
	 * 按主键update所有字段
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(T record);
	
	/**
	 * 用对象查询数据
	 * 
	 * @param 
	 * @return
	 */
	T selectOneByEntity(T obj);
	
	/**
	 * 用对象查询List数据
	 * 
	 * @param 
	 * @return
	 */
	List<T> selectByEntity(T obj);
	
	
	/**
	 * 查询所有被删除的数据(DelFlag=1）
	 * 
	 * @param 
	 * @return
	 */
	List<T> selectOnlyDelFlagData(T obj);

	/**
	 * 查询所有正常数据(DelFlag ！=1）
	 * 
	 * @param 
	 * @return
	 */
	List<T> selectWithOutDelFlagData(T obj);
	
	/**
	 * 查询所有数据
	 * @return
	 */
	List<T> selectAll();
		
	/**
	 * delete修改为历史数据修改del_flag by id
	 */
	int deleteToHisDataByKey(Integer id);
	
	/**
	 * delete修改为历史数据修改del_flag by Entity
	 */
	int deleteToHisDataByEntity(T obj);
	
}
