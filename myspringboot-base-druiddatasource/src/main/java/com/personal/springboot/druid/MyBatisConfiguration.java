package com.personal.springboot.druid;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

import com.github.pagehelper.PageHelper;
import com.personal.springboot.common.properties.PagehelperProperties;

/**
 * 扫描定义类
 * 
 * @author LiuBao
 * @version 2.0
 * 2017年3月27日
 *
 */
@Configuration
@AutoConfigureAfter({ DruidDataSourceConfiguration.class })
@MapperScan(basePackages = "com.personal.springboot.**.dao.mapper")
public class MyBatisConfiguration {
    
    public final static Logger LOGGER = LoggerFactory.getLogger(MyBatisConfiguration.class);  

    @Value("${mybatis.config.location:/mybatis-config.xml}")
    private Resource configLocation;
    
    @Autowired(required=false)
    private PagehelperProperties pagehelperProperties;
    
    @Bean
    public SqlSessionFactory buildSqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(configLocation);
        sessionFactory.setTypeAliasesPackage("com.personal.springboot.user.dao.entity");
        PageHelper pageHelper = new PageHelper();  
        Properties props = new Properties();  
        props.setProperty("reasonable", pagehelperProperties.getReasonable());  
        props.setProperty("supportMethodsArguments", pagehelperProperties.getSupportMethodsArguments());  
        props.setProperty("returnPageInfo", pagehelperProperties.getReturnPageInfo());  
        props.setProperty("params", pagehelperProperties.getParams());  
        pageHelper.setProperties(props);  
        sessionFactory.setPlugins(new Interceptor[]{pageHelper});  
        LOGGER.warn("SqlSessionFactory初始化完成了...");
        return sessionFactory.getObject();
    }
    
    @Autowired
    @Bean(name = "sqlSessionTemplate", destroyMethod = "close" )
    @Scope("prototype")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
}
