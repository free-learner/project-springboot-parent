package com.personal.springboot.multidatasource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.common.aop.DataSourceType;
import com.personal.springboot.common.exception.BaseServiceException;

/**
 * 本地线程全局变量，用来存放当前操作是读还是写
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月27日
 */
public class DataSourceContextHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);
    
    private static final ThreadLocal<String> local = new ThreadLocal<String>();
    public static final List<DataSourceType> dataSourceTypes = new ArrayList<DataSourceType>();
    
    static{
        dataSourceTypes.add(DataSourceType.WRITE);
        dataSourceTypes.add(DataSourceType.READ);
    }

    public static ThreadLocal<String> getLocal() {
        return local;
    }

    /**
     * 读可能是多个库
     */
    public static void read() {
        LOGGER.info("设置当前模式为:{}",DataSourceType.READ.getType());
        local.set(DataSourceType.READ.getType());
    }

    /**
     * 写只有一个库
     */
    public static void write() {
        LOGGER.info("设置当前模式为:{}",DataSourceType.WRITE.getType());
        local.set(DataSourceType.WRITE.getType());
    }

    /**
     * 设置指定的读写类型
     */
    public static void setDataSourceType(DataSourceType type) {
        if(type==null){
            throw new BaseServiceException("设置指定的读写类型参数信息DataSourceType为空!");
        }
        LOGGER.info("设置当前模式为:{}",type.getType());
        switch (type) {
        case WRITE:
            local.set(DataSourceType.WRITE.getType());
            break;
        case READ:
            local.set(DataSourceType.READ.getType());
            break;
        default:
            LOGGER.warn("设置当前模式非READ非WRITE,设置为默认WRITE方式");
            break;
        }
    }

    /**
     * 这里需要注意的时，每次我们返回当前数据源的值得时候都需要移除ThreadLocal的值,
     *  这是为了避免同一线程上一次方法调用对之后调用的影响
     */
    public static void remove() {
        LOGGER.info("清除当前模式为:{}",local.get());
        //local.remove();
        local.set(null);
    }
    
    public static String getJdbcType() {
        String jdbcType = local.get();
        LOGGER.info("获取当前模式为:{}",jdbcType);
        return jdbcType;
    }

    /**
     * 是否包含
     */
    public static boolean contains(DataSourceType dsType) {
        for (DataSourceType dataSourceType : DataSourceType.values()) {
            if(dataSourceType.equals(dsType)){
                return true;
            }
        }
        return false;
        //return dataSourceTypes.contains(dsType);
    }
}
