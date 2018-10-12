package com.personal.springboot.multidatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月27日
 */
@Configuration
@EnableTransactionManagement
@Order(Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({ DataSourceConfiguration.class })
public class MyDataSourceTransactionManagerAutoConfiguration extends DataSourceTransactionManagerAutoConfiguration {
    /**
     * 自定义事务
     * MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用。
     */
//    @Autowired
//    private AbstractRoutingDataSource dynamicDataSource;
//    
//    @Override
//    @Bean(name = "transactionManager")
//    public DataSourceTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dynamicDataSource);
//    }
    
    @Autowired
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(AbstractRoutingDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
    
//  @Bean
//  public DataSourceTransactionManager createTransactionManager(AbstractRoutingDataSource dynamicDataSource) {
//      DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dynamicDataSource);
//      return dataSourceTransactionManager;
//  }
//  
//  @Autowired
//  @Bean(name = "transactionManager")
//  protected PlatformTransactionManager createTransactionManager(AbstractRoutingDataSource dynamicDataSource) {
//      return new DataSourceTransactionManager(dynamicDataSource);
//  }
//    
//  @Override
//  @Bean(name = "transactionManager")
//  public DataSourceTransactionManager transactionManager() {
////      log.info("-------------------- transactionManager init ---------------------");
////      return new DataSourceTransactionManager(SpringContextHolder.getBean("roundRobinDataSouceProxy"));
////      return new DataSourceTransactionManager(SpringContextHolder.getBean("roundRobinDataSouceProxy"));
////      return new DataSourceTransactionManager((AbstractRoutingDataSource)SpringContextHolder.getBean("roundRobinDataSouceProxy"));
//      return new DataSourceTransactionManager(SpringContextHolder.getBean(AbstractRoutingDataSource.class));
//  }
}