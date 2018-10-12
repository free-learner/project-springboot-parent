package com.personal.springboot.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.personal.springboot.common.entity.BaseEntity;

/**
 * 基础的增删改查接口定义
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public interface BaseDAO<T extends BaseEntity> {

    /**
     * 查询所有数据
     */
    List<T> selectAll();

    /**
     * 用对象查询List数据
     */
    List<T> selectByEntity(T entity);

    /**
     * 按主键查询，返回实体类
     */
    T selectByPrimaryKey(Long id);

    /**
     * 按code查询，返回实体类
     */
    T selectByCode(String code);

    /**
     * 分页条件查询
     * 
     * RowBounds:
     * @param pageIndex 起始角标
     * @param pageSize 分页条数
     * @param T entity 包含删除的历史数据
     * @return List<T>
     */
    public List<T> selectListByPage(T entity, RowBounds rowBounds);

    /**
     * 分页条件查询总数
     * 
     * @param T 包含删除的历史数据
     * @return int
     */
    public int selectCount(T entity);
    
    public int count(T entity);

    /**
     * 按实体类所有字段插入数据
     */
    int insert(T entity);

    /**
     * 按实体类选择非空字段插入数据
     */
    int insertSelective(T entity);

    /**
     * 按实体类所有字段插入数据
     */
    int insertBatch(List<T> entitys);
    
    /**
     * 按主键update所有字段
     */
    int updateByPrimaryKey(T entity);

    /**
     * 按主键update非空字段
     */
    int updateByPrimaryKeySelective(T entity);

    /**
     * 按code业务码update所有字段
     */
    int updateByCode(T entity);

    /**
     * 按code业务码update非空字段
     */
    int updateByCodeSelective(T entity);

    /**
     * 按主键id删除数据
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 按code删除数据
     */
    int deleteByCode(String code);

}
