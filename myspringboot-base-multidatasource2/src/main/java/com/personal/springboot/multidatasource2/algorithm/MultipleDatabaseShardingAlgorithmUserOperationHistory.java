package com.personal.springboot.multidatasource2.algorithm;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.MultipleKeysDatabaseShardingAlgorithm;

/**
 * UserOperationHistory表分库的逻辑函数 
 * 根据code<String>。。。字段进行对应的分库操作
 * 
 * @Author LiuBao
 * @Version 2.0 2017年5月15日
 */
public class MultipleDatabaseShardingAlgorithmUserOperationHistory extends HashCodeSingleStringKeyAlgorithm implements MultipleKeysDatabaseShardingAlgorithm {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleDatabaseShardingAlgorithmUserOperationHistory.class);

    /**
     * 根据分片值计算分片结果名称集合.
     *
     * @param availableTargetNames 所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardingCloumeValues 分片值集合
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    /**
     * 分库分表逻辑:<2个数据库>
     * 1.【分库】选择,按照userCode(mobilePhone)进行查询;
     * sharding_0:18611478782,18611478784,18611478786,18611478788,...
     * sharding_1:18611478781,18611478783,18611478785,18611478787,...
     * 2.分表选择:
     *      2.1按照mobilePhone(userCode)进行查询<4张表>:
     *          user_operation_history_0:18611478784,18611478788,...
     *          user_operation_history_1:18611478781,18611478785,...
     *          user_operation_history_2:18611478782,18611478786,...
     *          user_operation_history_3:18611478783,18611478787,...
     *      2.1按照mobilePhone(userCode)进行查询<3张表>:
     *          user_operation_history_0:18611478783,18611478786,...
     *          user_operation_history_1:18611478781,18611478784,...
     *          user_operation_history_2:18611478782,18611478785,...
     *          
     *      2.2如果createDate不为空,表名按照年月格式查询
     *          user_operation_history_201705:查询参数createDate的值中年月为201705
     *          user_operation_history_201706:查询参数createDate的值中年月为201706
     *      
     * 3.添加操作,或者更新等操作无法在此处区分;
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public Collection<String> doSharding(final Collection<String> availableTargetDatabaseNames, final Collection<ShardingValue<?>> shardingCloumeValues) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("doSharding参数信息:availableTargetDatabaseNames={}", ArrayUtils.toString(availableTargetDatabaseNames));
            LOGGER.info("doSharding参数信息:shardingCloumeValues={}", ArrayUtils.toString(shardingCloumeValues));
        }
        
        int size = availableTargetDatabaseNames.size();
        Collection<String> realDatabaseNames = new LinkedHashSet<>(size);
        String suffix = null;
        String logicTableName = null;
        //String userCode = null;
        String mobilePhone = null;
        Date createDate = null;
        for (ShardingValue<?> shardingValue : shardingCloumeValues) {
            logicTableName = shardingValue.getLogicTableName();
            if (shardingValue.getColumnName().equals("mobile_phone")) {
                mobilePhone = ((ShardingValue<String>) shardingValue).getValue();
            }
            if (shardingValue.getColumnName().equals("create_date")) {
                createDate = ((ShardingValue<Date>) shardingValue).getValue();
            }
            /*if (shardingValue.getColumnName().equals("user_code")) {
                userCode = ((ShardingValue<String>) shardingValue).getValue();
            }
            if (StringUtils.isNoneBlank(userCode)) {
                //按照userCode進行分表查詢
                suffix = super.algorithmTable(userCode, size,logicTableName);
                break;
            }else */if (StringUtils.isNoneBlank(mobilePhone)){
                //全部库名
                //suffix="";
                //realDatabaseNames.addAll(availableTargetDatabaseNames);
                //按照mobilePhone進行分表查詢
                suffix = super.algorithmTable(Long.valueOf(mobilePhone), size,logicTableName);
                break;
            }else  if (createDate!=null) {
                //按照日期進行分表查詢[没有插入],这时使用全库
                realDatabaseNames.addAll(availableTargetDatabaseNames);
                break;
            }
        }
        
        LOGGER.info("doSharding参数信息,计算得到的suffix={}", suffix);
        if(StringUtils.isNoneBlank(suffix)){
            for (String databaseName : availableTargetDatabaseNames) {
                if (databaseName.endsWith(suffix)) {
                    realDatabaseNames.add(databaseName);
                }
            }
        }
        
        //String realDatabaseName = logicTableName + suffix;
        //realDatabaseNames.add(realDatabaseName);
        //realDatabaseNames.addAll(availableTargetDatabaseNames);
        LOGGER.warn("availableTargetDatabaseNames匹配结果为:【{}】", ArrayUtils.toString(realDatabaseNames));
        return realDatabaseNames;
    }

}
