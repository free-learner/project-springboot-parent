package com.personal.springboot.service;

import java.util.List;

/**
 * service层统一服务接口定义
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public interface BaseService<T> {
    
    /**
     * 查询所有数据
     */
    List<T> selectAll();
    
    /**
     * 用对象查询List数据
     */
    List<T> selectByEntity(T record);
    
    /**
     * 按主键查询数据
     */
    T selectByPrimaryKey(Long id);
    
    /**
     * 按业务code查询数据
     */
    T selectByCode(String code);
    
    /**
     * 分页条件查询
     * @param pageIndex 起始角标
     * @param pageSize  分页条数
     * @param T record 包含删除的历史数据
     * @return  List<T>
     */
    public List<T> selectListByPage(T record,int pageIndex, int pageSize);
    
    /**
     * 分页条件查询总数
     * @param T record 包含删除的历史数据
     * @return int
     */
    public int selectCount(T record);
    
    /**
     * 按实体类所有字段插入数据
     */
    int insert(T record);
    
    /**
     * 按实体类所有字段批量插入数据
     */
    boolean insertBatch(List<T> records);

    /**
     * 按实体类选择非空字段插入数据
     */
    int insertSelective(T record);

    /**
     * 按主键update所有字段
     */
    int updateByPrimaryKey(T record);
    
    /**
     * 按主键update非空字段
     */
    int updateByPrimaryKeySelective(T record);
    
    /**
     * 按code业务码update所有字段
     */
    int updateByCode(T record);
    
    /**
     * 按code业务码update非空字段
     */
    int updateByCodeSelective(T record);

    /**
     * 按主键删除数据
     */
    int deleteByPrimaryKey(Long id);
    
    /**
     * 按业务code删除数据
     */
    int deleteByCode(String code);
    
}
