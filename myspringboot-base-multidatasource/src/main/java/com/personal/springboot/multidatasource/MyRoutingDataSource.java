package com.personal.springboot.multidatasource;


import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.personal.springboot.common.aop.DataSourceType;

/**
 * 路由控制
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月28日
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRoutingDataSource.class);
//
//    private static final ThreadLocal<String> dataSourceKey = new ThreadLocal<String>();
//
//    public static void setDataSourceKey(String dataSource) {
//        dataSourceKey.set(dataSource);
//    }
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        String dsName = dataSourceKey.get();
//        // 这里需要注意的时，每次我们返回当前数据源的值得时候都需要移除ThreadLocal的值，
//        // 这是为了避免同一线程上一次方法调用对之后调用的影响
//        dataSourceKey.remove();
//        return dsName;
//    }
    
    private final int dataSourceNumber;
    private AtomicInteger count = new AtomicInteger(0);
    
    public MyRoutingDataSource(int dataSourceNumber) {
        this.dataSourceNumber = dataSourceNumber;
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        // 读库简单负载均衡
        int number = count.getAndAdd(1);
        Object resultObject=null;
        String typeKey = DataSourceContextHolder.getJdbcType();
        //只对主库开启事务，如果typeKey为空表示获取主库的datasource
        if (StringUtils.isBlank(typeKey)
                ||DataSourceType.WRITE.getType().equals(typeKey)){
            resultObject= DataSourceType.WRITE.getType();
        }else{
            int lookupKey = number % dataSourceNumber;
            LOGGER.info("当前[dataSourceNumber={},lookupKey={}]",dataSourceNumber,lookupKey);
            switch (lookupKey) {
            case 0:
            case 1:
                resultObject= DataSourceType.READ.getType()+(lookupKey+1);
                break;
            default:
                resultObject= DataSourceType.READ.getType()+1;
                break;
            }
        }
        LOGGER.info("determineCurrentLookupKey获取的当前[number={}]数据源名称为:{}",number,resultObject);
        //通过aop实现移除了
        //DataSourceContextHolder.remove();
        return resultObject;
    }
}