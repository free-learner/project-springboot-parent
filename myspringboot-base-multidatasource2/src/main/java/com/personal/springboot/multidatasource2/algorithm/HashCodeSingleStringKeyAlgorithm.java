package com.personal.springboot.multidatasource2.algorithm;

import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;

/**
 * 统一的hashCode计算类
 * <code>==><String>
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月16日
 */
public abstract class HashCodeSingleStringKeyAlgorithm{
    private static final Logger LOGGER = LoggerFactory.getLogger(HashCodeSingleStringKeyAlgorithm.class);
    
    private static final int tableAliasSizeDefault=3;
    private static final int databaseSizeDefault=2;
    
    /**
     * ShardingValue<String>类型方法定义
     */
    public String algorithmTable(ShardingValue<String> shardingCloumeValue,int tableAliasSize) {
        String logicTableName = shardingCloumeValue.getLogicTableName();
        Collection<String> values = shardingCloumeValue.getValues();
        LOGGER.info("algorithmTable.shardingCloumeValue.getValues结果为:{}",ArrayUtils.toString(values));
        if(tableAliasSize<=0){
            tableAliasSize=tableAliasSizeDefault;
        }
        return algorithmTable(shardingCloumeValue.getValue(),tableAliasSize,logicTableName);
    }

    public String algorithmTable(String shardingCloume,int tableAliasSize,String logicTableName) {
        if(tableAliasSize<=0){
            tableAliasSize=tableAliasSizeDefault;
        }
        String hashCodeKey=Math.abs(shardingCloume.hashCode() % tableAliasSize)+"";
        LOGGER.info("algorithmTable接口统一计算结果为:logicTableName={},hashCodeKey={},tableSize={}",logicTableName,hashCodeKey,tableAliasSize);
        return hashCodeKey;
    }
    
    public String algorithmDatabase(ShardingValue<String> shardingCloumeValue,int databaseSize) {
        String logicTableName = shardingCloumeValue.getLogicTableName();
        Collection<String> values = shardingCloumeValue.getValues();
        LOGGER.info("algorithmDatabase.shardingCloumeValue.getValues结果为:{}",ArrayUtils.toString(values));
        if(databaseSize<=0){
            databaseSize=databaseSizeDefault;
        }
        return algorithmDatabase(shardingCloumeValue.getValue(),databaseSize,logicTableName);
    }
    
    public String algorithmDatabase(String shardingCloume,int databaseSize,String logicTableName) {
        if(databaseSize<=0){
            databaseSize=databaseSizeDefault;
        }
        String hashCodeKey=Math.abs(shardingCloume.hashCode() % databaseSize)+"";
        LOGGER.info("algorithmDatabase接口统一计算结果为:logicTableName={},hashCodeKey={},tableSize={}",logicTableName,hashCodeKey,databaseSize);
        return hashCodeKey;
    }
    
    /**
     * ShardingValue<Long>类型方法定义
     * 按照奇数/偶数<...>进行划分
     */
    public String algorithmTableLong(ShardingValue<Long> shardingCloumeValue,int tableAliasSize) {
        String logicTableName = shardingCloumeValue.getLogicTableName();
        Collection<Long> values = shardingCloumeValue.getValues();
        LOGGER.info("algorithmTableLong.shardingCloumeValue.getValues结果为:{}",ArrayUtils.toString(values));
        if(tableAliasSize<=0){
            tableAliasSize=tableAliasSizeDefault;
        }
        return algorithmTable(shardingCloumeValue.getValue(),tableAliasSize,logicTableName);
    }

    public String algorithmTable(Long shardingCloume,int tableAliasSize,String logicTableName) {
        if(tableAliasSize<=0){
            tableAliasSize=tableAliasSizeDefault;
        }
        String hashCodeKey=Math.abs(shardingCloume % tableAliasSize)+"";
        LOGGER.info("algorithmTableLong接口统一计算结果为:logicTableName={},hashCodeKey={},tableSize={}",logicTableName,hashCodeKey,tableAliasSize);
        return hashCodeKey;
    }
    
    public String algorithmDatabaseLong(ShardingValue<Long> shardingCloumeValue,int databaseSize) {
        String logicTableName = shardingCloumeValue.getLogicTableName();
        Collection<Long> values = shardingCloumeValue.getValues();
        LOGGER.info("algorithmDatabaseLong.shardingCloumeValue.getValues结果为:{}",ArrayUtils.toString(values));
        if(databaseSize<=0){
            databaseSize=databaseSizeDefault;
        }
        return algorithmDatabase(shardingCloumeValue.getValue(),databaseSize,logicTableName);
    }
    
    public String algorithmDatabase(Long shardingCloume,int databaseSize,String logicTableName) {
        if(databaseSize<=0){
            databaseSize=databaseSizeDefault;
        }
        String hashCodeKey=Math.abs(shardingCloume % databaseSize)+"";
        LOGGER.info("algorithmDatabaseLong接口统一计算结果为:logicTableName={},hashCodeKey={},tableSize={}",logicTableName,hashCodeKey,databaseSize);
        return hashCodeKey;
    }
    
    
    
}