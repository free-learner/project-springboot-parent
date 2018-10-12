package com.personal.springboot.multidatasource;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import com.personal.springboot.common.aop.DataSourceType;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月27日
 */
@Configuration
public class DataSourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);
    
    @Value("${datasource.type}")
    private Class<? extends DataSource> datasourceType;
    //private Class<? extends DataSource> datasourceType = com.alibaba.druid.pool.DruidDataSource.class;
    
  @Value("${datasource.readSize}")
  private int dataSourceReadSize;

    @Primary
    @Bean(destroyMethod = "close" ,name = "writeDataSource")
    @ConfigurationProperties(prefix = "datasource.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().type(datasourceType).build();
    }

    @Bean(destroyMethod = "close" ,name = "readDataSource1")
    @ConfigurationProperties(prefix = "datasource.read1")
    public DataSource readDataSource1() {
        return DataSourceBuilder.create().type(datasourceType).build();
    }

    @Bean(destroyMethod = "close" ,name = "readDataSource2")
    @ConfigurationProperties(prefix = "datasource.read2")
    public DataSource readDataSourceTwo() {
        return DataSourceBuilder.create().type(datasourceType).build();
    }
    
    /**
     * 有多少个数据源就要配置多少个bean
     */
    @Bean(name = "dynamicDataSource")
    public AbstractRoutingDataSource dynamicDataSource(@Qualifier("writeDataSource") DataSource writeDataSource,
            @Qualifier("readDataSource1") DataSource readDataSource1,
            @Qualifier("readDataSource2") DataSource readDataSource2) {
        MyRoutingDataSource proxy = new MyRoutingDataSource(dataSourceReadSize);

        Assert.notNull(writeDataSource,"数据源writeDataSource为空!");
        Assert.notNull(readDataSource1,"数据源readDataSource1为空!");
        Assert.notNull(readDataSource2,"数据源readDataSource2为空!");

        //表示可用的数据源,包括写和读数据源
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        // 写
        targetDataSources.put(DataSourceType.WRITE.getType(), writeDataSource);

        // 如果有多个DataSource,需要设置多个
        for (int i = 1; i <= dataSourceReadSize; i++) {
            switch (i) {
            case 1:
                targetDataSources.put(DataSourceType.READ.getType() + i, readDataSource1);
                LOGGER.warn("case1条件执行了");
                break;
            case 2:
                targetDataSources.put(DataSourceType.READ.getType() + i, readDataSource2);
                LOGGER.warn("case2条件执行了");
                break;
            default:
                break;
            }
        }
        //targetDataSources.put(DataSourceType.READ.getType(), readDataSource1);

        //设置默认的数据源为写数据源
        proxy.setDefaultTargetDataSource(writeDataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }
    
}