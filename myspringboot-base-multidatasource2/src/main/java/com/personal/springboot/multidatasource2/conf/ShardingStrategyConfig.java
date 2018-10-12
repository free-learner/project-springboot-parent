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
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.NoneDatabaseShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.time.AbstractClock;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.personal.springboot.multidatasource2.algorithm.MultipleDatabaseShardingAlgorithmUserOperationHistory;
import com.personal.springboot.multidatasource2.algorithm.MultipleTableShardingAlgorithmUserOperationHistory;
import com.personal.springboot.multidatasource2.algorithm.SingleKeyDatabaseShardingAlgorithmLoanUser;
import com.personal.springboot.multidatasource2.algorithm.SingleKeyDatabaseShardingAlgorithmUserOperationHistoryCreateDate;
import com.personal.springboot.multidatasource2.algorithm.SingleKeyTableShardingAlgorithmLoanUser;
import com.personal.springboot.multidatasource2.algorithm.SingleKeyTableShardingAlgorithmUserOperationHistoryCreateDate;

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
public class ShardingStrategyConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingStrategyConfig.class);

    /**
     * TODO 待添加位置
     */
    @Bean(name = "shardingRule")
    public ShardingRule shardingRule(DataSourceRule dataSourceRule) {
        Collection<TableRule> tableRules = new ArrayList<>();
        tableRules.add(tableRuleLoanUser(dataSourceRule));
        tableRules.add(tableRuleUserOperationHistory(dataSourceRule));
        //tableRules.add(tableRuleUserOperationHistoryDate(dataSourceRule));
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule).tableRules(tableRules)
                //.databaseShardingStrategy(databaseShardingSingleKeyStrategyLoanUser()).tableShardingStrategy(tableShardingSingleKeyStrategyLoanUser())
                //.databaseShardingStrategy(multipleDatabaseShardingAlgorithmUserOperationHistory()).tableShardingStrategy(multipleTableShardingAlgorithmUserOperationHistory())
                .build();
        LOGGER.info("ShardingRule初始化完成...");
        return shardingRule;
    }
    
    /**
     * TODO 待添加位置NoneDatabaseShardingAlgorithm
     */
    @Bean(name = "tableRuleUserOperationHistoryDate")
    public TableRule tableRuleUserOperationHistoryDate(DataSourceRule dataSourceRule) {
        String logicTable = "user_operation_history_v";
        List<String> actualTables = new ArrayList<>();
        actualTables.add("user_operation_history_v_201704");
        actualTables.add("user_operation_history_v_201705");
        actualTables.add("user_operation_history_v_201706");
        
        String shardingColumn = "create_date";
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy(shardingColumn,
                new SingleKeyTableShardingAlgorithmUserOperationHistoryCreateDate());
        //默认的类型为String
        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy(shardingColumn,
                new SingleKeyDatabaseShardingAlgorithmUserOperationHistoryCreateDate());
                //new NoneDatabaseShardingAlgorithm());
        
        TableRule tableRule = TableRule.builder(logicTable).actualTables(actualTables).dataSourceRule(dataSourceRule)
                .databaseShardingStrategy(databaseShardingStrategy)
                .tableShardingStrategy(tableShardingStrategy)
                .build();
        LOGGER.info("tableRuleUserOperationHistoryDate初始化完成...");
        return tableRule;
    }
    
    
    /**
     * TODO 待添加位置
     */
    @Bean(name = "tableRuleUserOperationHistory")
    public TableRule tableRuleUserOperationHistory(DataSourceRule dataSourceRule) {
        String logicTable = "user_operation_history";
        List<String> actualTables = new ArrayList<>();
        actualTables.add("user_operation_history_0");
        actualTables.add("user_operation_history_1");
        actualTables.add("user_operation_history_2");
        //actualTables.add("user_operation_history_3");
        //actualTables.add("user_operation_history_201704");
        //actualTables.add("user_operation_history_201705");
        //actualTables.add("user_operation_history_201706");
        TableRule tableRule = TableRule.builder(logicTable).actualTables(actualTables).dataSourceRule(dataSourceRule)
                .databaseShardingStrategy(multipleDatabaseShardingAlgorithmUserOperationHistory()).tableShardingStrategy(multipleTableShardingAlgorithmUserOperationHistory())
                .build();
        LOGGER.info("tableRuleUserOperationHistory初始化完成...");
        return tableRule;
    }
    @Bean(name = "multipleTableShardingAlgorithmUserOperationHistory")
    public TableShardingStrategy multipleTableShardingAlgorithmUserOperationHistory() {
        Collection<String> shardingColumns=new  ArrayList<>();
        //shardingColumns.add("user_code");
        shardingColumns.add("mobile_phone");
        //shardingColumns.add("create_date");
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy(shardingColumns,
                new MultipleTableShardingAlgorithmUserOperationHistory());
        LOGGER.info("multipleTableShardingAlgorithmUserOperationHistory初始化完成...");
        return tableShardingStrategy;
    }
    @Bean(name = "multipleDatabaseShardingAlgorithmUserOperationHistory")
    public DatabaseShardingStrategy multipleDatabaseShardingAlgorithmUserOperationHistory() {
        Collection<String> shardingColumns=new  ArrayList<>();
        //shardingColumns.add("user_code");
        shardingColumns.add("mobile_phone");
        //shardingColumns.add("create_date");
        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy(shardingColumns,
                new MultipleDatabaseShardingAlgorithmUserOperationHistory());
        LOGGER.info("multipleDatabaseShardingAlgorithmUserOperationHistory初始化完成...");
        return databaseShardingStrategy;
    }
    
    /**
     * TODO 待添加位置
     */
    @Bean(name = "tableRuleLoanUser")
    public TableRule tableRuleLoanUser(DataSourceRule dataSourceRule) {
        String logicTable = "loan_user";
        List<String> actualTables = new ArrayList<>();
        actualTables.add("loan_user_0");
        actualTables.add("loan_user_1");
        actualTables.add("loan_user_2");
        TableRule tableRule = TableRule.builder(logicTable).actualTables(actualTables).dataSourceRule(dataSourceRule)
                .databaseShardingStrategy(databaseShardingSingleKeyStrategyLoanUser()).tableShardingStrategy(tableShardingSingleKeyStrategyLoanUser())
                .build();
        LOGGER.info("tableRuleLoanUser初始化完成...");
        return tableRule;
    }
    @Bean(name = "tableShardingSingleKeyStrategyLoanUser")
    public TableShardingStrategy tableShardingSingleKeyStrategyLoanUser() {
        //String shardingColumn = "code";
        String shardingColumn = "mobile_phone";
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy(shardingColumn,
                new SingleKeyTableShardingAlgorithmLoanUser());
        LOGGER.info("tableShardingSingleKeyStrategyLoanUser初始化完成...");
        return tableShardingStrategy;
    }
    @Bean(name = "databaseShardingSingleKeyStrategyLoanUser")
    public DatabaseShardingStrategy databaseShardingSingleKeyStrategyLoanUser() {
        //String shardingColumn = "code";
        String shardingColumn = "mobile_phone";
        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy(shardingColumn,
                new SingleKeyDatabaseShardingAlgorithmLoanUser());
        LOGGER.info("databaseShardingSingleKeyStrategyLoanUser初始化完成...");
        return databaseShardingStrategy;
    }

}