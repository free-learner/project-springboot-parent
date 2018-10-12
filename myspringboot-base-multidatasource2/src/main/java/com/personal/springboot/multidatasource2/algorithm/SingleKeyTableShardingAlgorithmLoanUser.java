package com.personal.springboot.multidatasource2.algorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
/**
 *  LoanUser表分表的逻辑函数
 *  根据code<String>字段进行对应的分表操作
 *  
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月15日
 */
public class SingleKeyTableShardingAlgorithmLoanUser extends HashCodeSingleStringKeyAlgorithm implements SingleKeyTableShardingAlgorithm<String>{
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleKeyTableShardingAlgorithmLoanUser.class);
    
    /**
     * sql 中 = 操作时，table的映射
     */
    @Override
    public String doEqualSharding(final Collection<String> availabletableNames, final ShardingValue<String> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doInSharding参数信息:availabletableNames={}",ArrayUtils.toString(availabletableNames));
            LOGGER.info("doInSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        int size = availabletableNames.size();
        for (String tableName : availabletableNames) {
            String hashCodeKey= super.algorithmTable(shardingCloumeValue,size);
            if (tableName.endsWith(hashCodeKey)) {
                LOGGER.warn("availabletableNames匹配结果为:【{}】",tableName);
                return tableName;
            }
        }
        throw new IllegalArgumentException("表规则信息为找到,匹配失败!");
    }

    /**
     * sql 中 in 操作时，table的映射
     */
    @Override
    public Collection<String> doInSharding(final Collection<String> availabletableNames, final ShardingValue<String> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doInSharding参数信息:availabletableNames={}",ArrayUtils.toString(availabletableNames));
            LOGGER.info("doInSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        int size = availabletableNames.size();
        String logicTableName = shardingCloumeValue.getLogicTableName();
        Collection<String> result = new LinkedHashSet<String>(availabletableNames.size());
        for (String value : shardingCloumeValue.getValues()) {
            String hashCodeKey= super.algorithmTable(value,size,logicTableName);
            for (String tableName : availabletableNames) {
                if (tableName.endsWith(hashCodeKey)) {
                    result.add(tableName);
                }
            }
        }
        LOGGER.warn("availabletableNames匹配结果为:【{}】",ArrayUtils.toString(result));
        return result;
    }

    /**
     * sql 中 between 操作时，table的映射
     */
    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availabletableNames, final ShardingValue<String> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doBetweenSharding参数信息:availabletableNames={}",ArrayUtils.toString(availabletableNames));
            LOGGER.info("doBetweenSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        Collection<String> result = new LinkedHashSet<String>(availabletableNames);
        return result;
    }

}
