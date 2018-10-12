package com.personal.springboot.multidatasource2.algorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

/**
 *  LoanUser表分库的逻辑函数
 *  根据code<String>字段进行对应的分库操作
 *  
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月15日
 */
public class SingleKeyDatabaseShardingAlgorithmLoanUser extends HashCodeSingleStringKeyAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String>{
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleKeyDatabaseShardingAlgorithmLoanUser.class);
    
    /**
     * sql 中关键字 匹配符为 =的时候，表的路由函数
     */
    @Override
    public String doEqualSharding(final Collection<String> availableDatabase, final ShardingValue<String> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doEqualSharding参数信息:availableDatabase={}",ArrayUtils.toString(availableDatabase));
            LOGGER.info("doEqualSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        int size = availableDatabase.size();
        for (String database : availableDatabase) {
            String hashCodeKey= super.algorithmDatabase(shardingCloumeValue,size);
            if (database.endsWith(hashCodeKey)) {
                LOGGER.warn("availableDatabase匹配结果为:【{}】",database);
                return database;
            }
        }
        throw new IllegalArgumentException("表规则信息为找到,匹配失败!");
    }
    
    /**
     * sql 中关键字 匹配符为 in 的时候,有多个数值Collection，表的路由函数
     */
    @Override
    public Collection<String> doInSharding(final Collection<String> availableDatabase, final ShardingValue<String> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doInSharding参数信息:availableDatabase={}",ArrayUtils.toString(availableDatabase));
            LOGGER.info("doInSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        String logicTableName = shardingCloumeValue.getLogicTableName();
        int size = availableDatabase.size();
        Collection<String> result = new LinkedHashSet<String>(size);
        for (String shardingCloume : shardingCloumeValue.getValues()) {
            for (String database : availableDatabase) {
                String hashCodeKey= super.algorithmDatabase(shardingCloume,size,logicTableName);
                if (database.endsWith(hashCodeKey)) {
                    result.add(database);
                }
            }
        }
        LOGGER.warn("availableDatabase匹配结果为:【{}】",ArrayUtils.toString(result));
        return result;
    }

    /**
     * sql 中关键字 匹配符为 between的时候，表的路由函数
     */
    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availableDatabase,final ShardingValue<String> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doBetweenSharding参数信息:availableDatabase={}",ArrayUtils.toString(availableDatabase));
            LOGGER.info("doBetweenSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        Collection<String> result = new LinkedHashSet<String>(availableDatabase);
        return result;
    }

}
