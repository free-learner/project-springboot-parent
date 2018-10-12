package com.personal.springboot.multidatasource2.conf;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
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
@MapperScan(basePackages = "com.personal.springboot.**.dao.mapper")
@AutoConfigureAfter({ DataSourceConfiguration.class })
public class MyBatisConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisConfiguration.class);
    
//    @Value("${mybatis.config.location:/mybatis-config.xml}")
    @Value("${mybatis.config.location}")
    private Resource configLocation;
    
    @Value("${mybatis.mapper.locations}")
    private String mapperLocations;
    
    @Value("${mybatis.typealiases.package}")
    private String typeAliasesPackage;
    
    @Autowired(required=false)
    private PagehelperProperties pagehelperProperties;

    @Autowired
    @Bean("sqlSessionFactory")
    public SqlSessionFactory buildSqlSessionFactory(@Qualifier("shardingDataSource") ShardingDataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //设置mybatis的配置文件路径
        //sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactoryBean.setConfigLocation(configLocation);
        //设置数据源为动态数据源
        sqlSessionFactoryBean.setDataSource(shardingDataSource);
        //设置类型前缀包名，在mapper文件中就不用使用详细的包名了，直接使用类名。
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        
        PageHelper pageHelper = new PageHelper();  
        Properties props = new Properties();  
        props.setProperty("reasonable", pagehelperProperties.getReasonable());  
        props.setProperty("supportMethodsArguments", pagehelperProperties.getSupportMethodsArguments());  
        props.setProperty("returnPageInfo", pagehelperProperties.getReturnPageInfo());  
        props.setProperty("params", pagehelperProperties.getParams());  
        pageHelper.setProperties(props);  
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});  
        
        //配置路径匹配器，获取匹配的文件
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        //sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources("classpath:**/mapper/*Mapper.xml"));
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(mapperLocations));
        
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        LOGGER.info("SqlSessionFactory初始化完成...");
        return sqlSessionFactory;
    }
    
    @Autowired
    @Scope("prototype")
    @Bean(name = "sqlSessionTemplate", destroyMethod = "close")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        LOGGER.info("SqlSessionTemplate初始化完成...");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
}
