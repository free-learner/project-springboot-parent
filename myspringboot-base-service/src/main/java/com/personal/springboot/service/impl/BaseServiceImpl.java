package com.personal.springboot.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.personal.springboot.common.aop.DataSourceType;
import com.personal.springboot.common.aop.TargetDataSource;
import com.personal.springboot.common.entity.BaseEntity;
import com.personal.springboot.common.entity.Cacheable;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.HashShardUtils;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.dao.BaseDAO;
import com.personal.springboot.service.BaseService;
import com.personal.springboot.service.RedisCacheService;

/**
 * service层统一基础服务接口实现
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月11日
 */
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired(required=false)
    private RedisCacheService redisCacheService;

    private BaseDAO<T> baseDAO;
    
    public BaseServiceImpl( final BaseDAO<T> baseDAO ) {
        this.baseDAO = baseDAO;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = true)
    public List<T> selectAll() {
        return baseDAO.selectAll();
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = true)
    public List<T> selectByEntity(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("查询条件信息为:{}",JSON.toJSONString(record));
        }
        return baseDAO.selectByEntity(record);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = true)
    public T selectByPrimaryKey(Long id) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("查询条件信息为:{}",JSON.toJSONString(id));
        }
        return baseDAO.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = true)
    public T selectByCode(String code) {
        return baseDAO.selectByCode(code);
    }

    @Override
    @TargetDataSource(DataSourceType.READ)
    @Transactional(propagation=Propagation.REQUIRED,readOnly = true)
    public List<T> selectListByPage(T record, int pageIndex, int pageSize) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("查询条件信息为:{}",JSON.toJSONString(record));
        }
        String orderBy=null;
        if(record instanceof BaseEntity){
            orderBy=" ID DESC";
        }
        //PageHelper.startPage(pageIndex, pageSize,orderBy); 
        //设置查询别了的时候,不进行总数查询操作触发
        PageHelper.startPage(pageIndex, pageSize,Boolean.FALSE); 
        PageHelper.orderBy(orderBy);
        List<T> resultList = baseDAO.selectByEntity(record);
        //清除分页信息,可以不用
        //PageHelper.clearPage();
        return resultList;
        //return baseDAO.selectListByPage(record, makeRowBounds(pageIndex, pageSize));
    }

    @Override
    public int selectCount(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("查询条件信息为:{}",JSON.toJSONString(record));
        }
        return baseDAO.selectCount(record);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int insert(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("插入条件信息为:{}",JSON.toJSONString(record));
        }
        int result = baseDAO.insert(fillInsertBaseEntity(record));
        String redisKey = null;
        if (record instanceof Cacheable) {
            redisKey = ((Cacheable) record).getRedisKey();
        }
        if (StringUtils.isNotBlank(redisKey)) {
            /*String jsonString = JSON.toJSONString(record);
            putToRedisCache(redisKey, jsonString);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("保存数据到redis缓存:{}", jsonString);
            }*/
            if (isExistRedisCache(redisKey)) {
                deleteRedisCache(redisKey);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("insert.清空redis缓存数据redisKey:{}", redisKey);
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public boolean insertBatch(List<T> records) {
        int insertBatch = baseDAO.insertBatch(fillInsertBaseEntitys(records));
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("批量插入条件结果,更新记录条数为:{}条!",insertBatch);
        }
        return true;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int insertSelective(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("插入条件信息为:{}",JSON.toJSONString(record));
        }
        int result = baseDAO.insertSelective(fillInsertBaseEntity(record));
        String redisKey = null;
        if (record instanceof Cacheable) {
            redisKey = ((Cacheable) record).getRedisKey();
        }
        if (StringUtils.isNotBlank(redisKey)) {
           /* String jsonString = JSON.toJSONString(record);
            putToRedisCache(redisKey, jsonString);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("保存数据到redis缓存:{}", jsonString);
            }*/
            if (isExistRedisCache(redisKey)) {
                deleteRedisCache(redisKey);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("insertSelective.清空redis缓存数据redisKey:{}", redisKey);
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int updateByPrimaryKey(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("更新条件信息为:{}",JSON.toJSONString(record));
        }
        int result = baseDAO.updateByPrimaryKey(record);
        String redisKey = null;
        if (record instanceof Cacheable) {
            redisKey = ((Cacheable) record).getRedisKey();
        }
        //Boolean delFlag = record.getDelFlag();
        if (StringUtils.isNotBlank(redisKey)) {
            if (isExistRedisCache(redisKey)) {
                deleteRedisCache(redisKey);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("updateByPrimaryKey.清空redis缓存数据redisKey:{}", redisKey);
                }
            }/* else {
                String jsonString = JSON.toJSONString(record);
                putToRedisCache(redisKey, jsonString);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("保存数据到redis缓存:{}", jsonString);
                }
            }*/
        }
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int updateByPrimaryKeySelective(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("更新条件信息为:{}",JSON.toJSONString(record));
        }
        int result = baseDAO.updateByPrimaryKeySelective(record);
        String redisKey = null;
        if (record instanceof Cacheable) {
            redisKey = ((Cacheable) record).getRedisKey();
        }
        //Boolean delFlag = record.getDelFlag();
        if (StringUtils.isNotBlank(redisKey)) {
            if (isExistRedisCache(redisKey)) {
                deleteRedisCache(redisKey);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("updateByPrimaryKeySelective.清空redis缓存数据redisKey:{}", redisKey);
                }
            } /*else {
                String jsonString = JSON.toJSONString(record);
                putToRedisCache(redisKey, jsonString);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("保存数据到redis缓存:{}", jsonString);
                }
            }*/
        }
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int updateByCode(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("更新条件信息为:{}",JSON.toJSONString(record));
        }
        int result = baseDAO.updateByCode(record);
        String redisKey = null;
        if (record instanceof Cacheable) {
            redisKey = ((Cacheable) record).getRedisKey();
        }
        //Boolean delFlag = record.getDelFlag();
        if (StringUtils.isNotBlank(redisKey)) {
            if (isExistRedisCache(redisKey)) {
                deleteRedisCache(redisKey);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("updateByCode.清空redis缓存数据redisKey:{}", redisKey);
                }
            } /*else {
                String jsonString = JSON.toJSONString(record);
                putToRedisCache(redisKey, jsonString);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("保存数据到redis缓存:{}", jsonString);
                }
            }*/
        }
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int updateByCodeSelective(T record) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("更新条件信息为:{}",JSON.toJSONString(record));
        }
        int result = baseDAO.updateByCodeSelective(record);
        String redisKey = null;
        if (record instanceof Cacheable) {
            redisKey = ((Cacheable) record).getRedisKey();
        }
        //Boolean delFlag = record.getDelFlag();
        if (StringUtils.isNotBlank(redisKey)) {
            if (isExistRedisCache(redisKey)) {
                deleteRedisCache(redisKey);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("updateByCodeSelective.清空redis缓存数据redisKey:{}", redisKey);
                }
            } /*else {
                String jsonString = JSON.toJSONString(record);
                putToRedisCache(redisKey, jsonString);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("保存数据到redis缓存:{}", jsonString);
                }
            }*/
        }
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int deleteByPrimaryKey(Long id) {
        int result = baseDAO.deleteByPrimaryKey(id);
        return result;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int deleteByCode(String code) {
        int result = baseDAO.deleteByCode(code);
        return result;
    }

    /**
     * insert的时候,填充基本属性空值信息
     */
    protected <E extends BaseEntity> E fillInsertBaseEntity(E record) {
        if (StringUtils.isBlank(record.getCode())) {
            record.setCode(UUIDGenerator.generate());
        }
        if (record.getDelFlag() == null) {
            record.setDelFlag(Boolean.FALSE);
        }
        if (DateTimeUtil.isDatetimeZero(record.getUpdateDate())) {
            record.setUpdateDate(DateTimeUtil.currentTimestamp());
        }
        if (DateTimeUtil.isDatetimeZero(record.getCreateDate())) {
            record.setCreateDate(DateTimeUtil.currentTimestamp());
        }
        return record;
    }

    /**
     * 批量填充空属性信息
     */
    private List<T> fillInsertBaseEntitys(List<T> records) {
        if (!CollectionUtils.isEmpty(records)) {
            for (T record : records) {
                fillInsertBaseEntity(record);
            }
        }
        return records;
    }

    protected <V> V copyProperties(V dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest,orig);
        } catch (IllegalAccessException e) {
            LOGGER.error("属性拷贝方法copyProperties异常:[orig={}]信息异常:{}",orig,e);
            dest=null;
        } catch (InvocationTargetException e) {
            LOGGER.error("属性拷贝方法copyProperties异常:[orig={}]信息异常:{}",orig,e);
            dest=null;
        }
        if(dest!=null){
            LOGGER.info("属性拷贝方法转换非空实体结果为:{}",JSON.toJSONString(dest));
        }
        return dest;
    }
    
    /**
     * 参数 pageIndex pageSize Limit   comment
     *          0               2               0,2     第一页(已转换为1记录)
     *          1               2               0,2     第一页
     *          2               2               2,2     第二页
     *          3               2               4,2     第二页
     *          ......................................................
     *          6               2               (6-1)*2,2     第二页
     *          ......................................................
     *          m               n               (m-1)*n,n     第m页
     */
    protected RowBounds makeRowBounds(int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = (pageSize <= 0) ? 10 : pageSize;
        pageSize = pageSize > 1000 ? 1000 : pageSize;
        //pageSize = (pageSize <= 0 || pageSize > 1000) ? 10 : pageSize;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("分页参数为:【pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
        }
        return new RowBounds((pageIndex - 1) * pageSize, pageSize);
    }

    protected void putToRedisCache(String key, String value) {
        String hashKey = HashShardUtils.getShardNamespace(key, null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("从cache保存[key={}],计算得到的hashKey为:{}", key, hashKey);
        }
        redisCacheService.addByKey(key, value);
    }

    protected void deleteRedisCache(String key) {
        /*String hashKey = HashShardUtils.getShardNamespace(key, null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("从cache删除[key={}],计算得到的hashKey为:{}", key, hashKey);
        }*/
        redisCacheService.deleteByKey(key);
        /*try {
        } catch (IOException e) {
            throw new BaseServiceException("缓存信息删除失败!");
        }*/
    }
    
    protected boolean isExistRedisCache(String key) {
        boolean result =false;
        result = redisCacheService.existsByKey(key);
        /*try {
        } catch (IOException e) {
            LOGGER.error("从isExistRedisCache异常[key={}],计算得到的result为:{}", key,result ,e);
        }*/
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("从isExistRedisCache查询结构[key={}],计算得到的result为:{}", key,result );
        }
        return result;
    }

    protected String getFromRedisCache(String key) {
        String hashKey = HashShardUtils.getShardNamespace(key, null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("从cache查询[key={}],计算得到的hashKey为:{}", key, hashKey);
        }
        String value = null;
        value = redisCacheService.getString(key);
        /*try {
        } catch (IOException e) {
            throw new BaseServiceException("缓存获取添加失败!");
        }*/
        return value;
    }

    protected T getEntityFromRedisCache(String key, Class<T> clazz) {
        String hashKey = HashShardUtils.getShardNamespace(key, null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("从cache查询Array[key={}],计算得到的hashKey为:{}", key, hashKey);
        }
        T value = null;
        value = redisCacheService.getObject(key,clazz);
        /*try {
        } catch (IOException e) {
            throw new BaseServiceException("缓存获取Entity对象失败!");
        }*/
        return value;
    }
    
    protected List<T> getArrayEntityFromRedisCache(String key, Class<T> clazz) {
        String hashKey = HashShardUtils.getShardNamespace(key, null);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("从cache查询Array[key={}],计算得到的hashKey为:{}", key, hashKey);
        }
        String value = null;
        value = redisCacheService.getString(key);
        /*try {
        } catch (IOException e) {
            throw new BaseServiceException("缓存获取Array信息失败!");
        }*/
        if (value != null) {
            return JSON.parseArray(value, clazz);
        }
        return null;
    }

}
