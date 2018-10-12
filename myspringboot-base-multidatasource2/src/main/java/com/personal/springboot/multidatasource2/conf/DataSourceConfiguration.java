package com.personal.springboot.multidatasource2.conf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.time.AbstractClock;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.personal.springboot.multidatasource2.algorithm.MultipleDatabaseShardingAlgorithmUserOperationHistory;
import com.personal.springboot.multidatasource2.algorithm.MultipleTableShardingAlgorithmUserOperationHistory;
import com.personal.springboot.multidatasource2.algorithm.SingleKeyDatabaseShardingAlgorithmLoanUser;
import com.personal.springboot.multidatasource2.algorithm.SingleKeyTableShardingAlgorithmLoanUser;

/**
 * 多数据源信息配置
 * 注意:
 * 解决多个数据源注入报错的问题:
 * 方式1:
 * 使用@Primary在对应的主数据源上即可;
 * 方式2:在启动main方法类上面,添加注解
 * @EnableAutoConfiguration(exclude={  
        DataSourceAutoConfiguration.class,  
            HibernateJpaAutoConfiguration.class, 
            DataSourceTransactionManagerAutoConfiguration.class,  
            })  
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月27日
 */
@SuppressWarnings("unused")
@Configuration
public class DataSourceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${datasource.type}")
    private Class<? extends DataSource> datasourceType;

    @Value("${datasource.readSize}")
    private int dataSourceReadSize;

    /**
     * 有多少个数据源就要配置多少个bean
     */
    @Primary
    @Bean(name = "shardingDataSource")
    public ShardingDataSource shardingDataSource(ShardingRule shardingRule) {
        // ShardingDataSource shardingDataSource=new ShardingDataSource(shardingRule());
        DataSource shardingDataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        LOGGER.info("ShardingDataSource初始化完成...");
         Assert.notNull(shardingDataSource,"数据源shardingDataSource为空!");
        return (ShardingDataSource) shardingDataSource;
    }

    /**
     * TODO 待添加位置
     *//*
    @Bean(name = "shardingRule")
    public ShardingRule shardingRule() {
        Collection<TableRule> tableRules = new ArrayList<>();
        tableRules.add(tableRuleLoanUser());
        tableRules.add(tableRuleUserOperationHistory());
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule()).tableRules(tableRules)
                .databaseShardingStrategy(databaseShardingSingleKeyStrategyLoanUser()).tableShardingStrategy(tableShardingSingleKeyStrategyLoanUser())
                .databaseShardingStrategy(multipleDatabaseShardingAlgorithmUserOperationHistory()).tableShardingStrategy(multipleTableShardingAlgorithmUserOperationHistory())
                .build();
        LOGGER.info("ShardingRule初始化完成...");
        return shardingRule;
    }
    
    *//**
     * TODO 待添加位置
     *//*
    @Bean(name = "tableRuleUserOperationHistory")
    public TableRule tableRuleUserOperationHistory() {
        String logicTable = "user_operation_history";
        List<String> actualTables = new ArrayList<>();
        actualTables.add("user_operation_history_0");
        actualTables.add("user_operation_history_1");
        TableRule tableRule = TableRule.builder(logicTable).actualTables(actualTables).dataSourceRule(dataSourceRule())
                .build();
        LOGGER.info("tableRuleUserOperationHistory初始化完成...");
        return tableRule;
    }
    @Bean(name = "multipleTableShardingAlgorithmUserOperationHistory")
    public TableShardingStrategy multipleTableShardingAlgorithmUserOperationHistory() {
        Collection<String> shardingColumns=new  ArrayList<>();
        shardingColumns.add("user_code");
        shardingColumns.add("mobile_phone");
        shardingColumns.add("create_date");
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy(shardingColumns,
                new MultipleTableShardingAlgorithmUserOperationHistory());
        LOGGER.info("multipleTableShardingAlgorithmUserOperationHistory初始化完成...");
        return tableShardingStrategy;
    }
    @Bean(name = "multipleDatabaseShardingAlgorithmUserOperationHistory")
    public DatabaseShardingStrategy multipleDatabaseShardingAlgorithmUserOperationHistory() {
        Collection<String> shardingColumns=new  ArrayList<>();
        shardingColumns.add("user_code");
        shardingColumns.add("mobile_phone");
        shardingColumns.add("create_date");
        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy(shardingColumns,
                new MultipleDatabaseShardingAlgorithmUserOperationHistory());
        LOGGER.info("multipleDatabaseShardingAlgorithmUserOperationHistory初始化完成...");
        return databaseShardingStrategy;
    }
    
    *//**
     * TODO 待添加位置
     *//*
    @Bean(name = "tableRuleLoanUser")
    public TableRule tableRuleLoanUser() {
        String logicTable = "loan_user";
        List<String> actualTables = new ArrayList<>();
        actualTables.add("loan_user_0");
        actualTables.add("loan_user_1");
        actualTables.add("loan_user_2");
        TableRule tableRule = TableRule.builder(logicTable).actualTables(actualTables).dataSourceRule(dataSourceRule())
                .build();
        LOGGER.info("tableRuleLoanUser初始化完成...");
        return tableRule;
    }
    @Bean(name = "tableShardingSingleKeyStrategyLoanUser")
    public TableShardingStrategy tableShardingSingleKeyStrategyLoanUser() {
        String shardingColumn = "code";
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy(shardingColumn,
                new SingleKeyTableShardingAlgorithmLoanUser());
        LOGGER.info("tableShardingSingleKeyStrategyLoanUser初始化完成...");
        return tableShardingStrategy;
    }
    @Bean(name = "databaseShardingSingleKeyStrategyLoanUser")
    public DatabaseShardingStrategy databaseShardingSingleKeyStrategyLoanUser() {
        String shardingColumn = "code";
        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy(shardingColumn,
                new SingleKeyDatabaseShardingAlgorithmLoanUser());
        LOGGER.info("databaseShardingSingleKeyStrategyLoanUser初始化完成...");
        return databaseShardingStrategy;
    }*/
    
    

    @Bean(name = "dataSourceRule")
    public DataSourceRule dataSourceRule() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("sharding_0", shardingDataSource0());
        dataSourceMap.put("sharding_1", shardingDataSource1());
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
        LOGGER.info("DataSourceRule初始化完成...");
        return dataSourceRule;
    }

    @Bean(destroyMethod = "close", initMethod = "init", name = "shardingDataSource0")
    @ConfigurationProperties(prefix = "datasource.sharding0")
    public DataSource shardingDataSource0() {
        return DataSourceBuilder.create().type(datasourceType).build();
    }

    @Bean(destroyMethod = "close", initMethod = "init", name = "shardingDataSource1")
    @ConfigurationProperties(prefix = "datasource.sharding1")
    public DataSource shardingDataSource1() {
        return DataSourceBuilder.create().type(datasourceType).build();
    }

    @Bean
    public CommonSelfIdGenerator commonSelfIdGenerator() {
        CommonSelfIdGenerator.setClock(AbstractClock.systemClock());
        CommonSelfIdGenerator commonSelfIdGenerator = new CommonSelfIdGenerator();
        long sequence = commonSelfIdGenerator.getSequence();
        LOGGER.info("CommonSelfIdGenerator初始化完成,sequence={}...",sequence);
        return commonSelfIdGenerator;
    }
    
    /**
     * TransactionManager
     * 此处我们使用了弱事务机制，如下图所示，事务不是原子的，可能提交分库1事务后，提交分库2事务失败，造成跨库事务不一致。可以考虑sharding-
     * jdbc提供的柔性事务实现. 柔性事务目前支持最大努力送达，未来计划支持TCC（Try-Confirm-Cancel）。
     * 最大努力送达是当事务失败后通过最大努力反复尝试送达操作实现，是在假定数据库操作一定可以成功的前提下进行的，保证数据最终的一致性。
     * 其适用场景是幂等性操作，如根据主键删除数据、带主键地插入数据、更新记录最后状态（如商品上下架操作）。
     * 
     */
    @Autowired
    @Bean(name = "transactionManager")
    protected PlatformTransactionManager createTransactionManager(
            @Qualifier("shardingDataSource") ShardingDataSource shardingDataSource) {
        return new DataSourceTransactionManager(shardingDataSource);
    }

}