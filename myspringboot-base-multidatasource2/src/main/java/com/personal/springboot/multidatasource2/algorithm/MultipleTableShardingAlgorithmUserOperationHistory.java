package com.personal.springboot.multidatasource2.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.personal.springboot.common.utils.DatetimeUtils;
/**
 *  UserOperationHistory表分表的逻辑函数
 *  根据code<String>。。。字段进行对应的分表操作
 *  
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月15日
 */
public class MultipleTableShardingAlgorithmUserOperationHistory extends HashCodeSingleStringKeyAlgorithm implements MultipleKeysTableShardingAlgorithm{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleTableShardingAlgorithmUserOperationHistory.class);
    
    /**
     * 根据分片值计算分片结果名称集合.
     *
     * @param availableTargetNames 所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardingCloumeValues 分片值集合
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    /**
     * 分库分表逻辑:
     * 1.【分库】选择,按照userCode(mobilePhone)进行查询;
     * sharding_0:18611478782,18611478784,18611478786,18611478788,...
     * sharding_1:18611478781,18611478783,18611478785,18611478787,...
     * 2.分表选择,
     *      2.1按照mobilePhone(userCode)进行查询;
     *          user_operation_history_0:18611478784,18611478788,...
     *          user_operation_history_1:18611478781,18611478785,...
     *          user_operation_history_2:18611478782,18611478786,...
     *          user_operation_history_3:18611478783,18611478787,...
     *      2.2如果createDate不为空,表名按照年月格式查询
     *          user_operation_history_201705:查询参数createDate的值中年月为201705
     *          user_operation_history_201706:查询参数createDate的值中年月为201706
     *      
     * 3.添加操作,或者更新等操作无法在此处区分;
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public Collection<String> doSharding(final Collection<String> availableTargetTableNames, final Collection<ShardingValue<?>> shardingCloumeValues) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("doSharding参数信息:availableTargetTableNames={}", ArrayUtils.toString(availableTargetTableNames));
            LOGGER.info("doSharding参数信息:shardingCloumeValues={}", ArrayUtils.toString(shardingCloumeValues));
        }
        
        int size = availableTargetTableNames.size();
        Collection<String> realTableNames = new LinkedHashSet<>(size);
        String suffix = null;
        String logicTableName = null;
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
            
            if (StringUtils.isNoneBlank(mobilePhone)) {
                //按照手机号進行分表[插入]/查詢操作
                suffix = super.algorithmTable(Long.valueOf(mobilePhone), size,logicTableName);
                break;
            }else  if (createDate!=null) {
                //按照日期進行分表查詢[没有插入]
                String formatDate = DatetimeUtils.formatDate(createDate, "yyyyMM");
                suffix =  "_" + formatDate;
                @SuppressWarnings("unused")
                String realTableName = logicTableName + suffix;
                //realTableNames.add(realTableName);
                realTableNames.addAll(availableTargetTableNames);
                break;
            }
        }
        
        LOGGER.info("doSharding参数信息,计算得到的suffix={}", suffix);
        if(StringUtils.isNoneBlank(suffix)){
            for (String tableName : availableTargetTableNames) {
                if (tableName.endsWith(suffix)) {
                    realTableNames.add(tableName);
                }
            }
        }
        
        //realTableNames.addAll(availableTargetTableNames);
        LOGGER.warn("availableTargetTableNames匹配结果为:【{}】", ArrayUtils.toString(realTableNames));
        return realTableNames;
    }
    
    
    @SuppressWarnings("unchecked")
    //@Override
    public Collection<String> doSharding2(final Collection<String> availableTargetTableNames, final Collection<ShardingValue<?>> shardingCloumeValues) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("doSharding参数信息:availableTargetTableNames={}", ArrayUtils.toString(availableTargetTableNames));
            LOGGER.info("doSharding参数信息:shardingCloumeValues={}", ArrayUtils.toString(shardingCloumeValues));
        }
      
        int size = availableTargetTableNames.size();
        List<String> realTableNames = new ArrayList<>(size);
        
        Set<Integer> orderIdValueSet = getShardingValue(shardingCloumeValues, "order_id",String.class);
        Set<Integer> userIdValueSet = getShardingValue(shardingCloumeValues, "user_id",String.class);
        //userIdValueSet[10,11] + orderIdValueSet[101,102] => valueResult[[10,101],[10,102],[11,101],[11,102]]
        Set<List<Integer>> valueResult = Sets.cartesianProduct(userIdValueSet, orderIdValueSet);
        
        for (List<Integer> value : valueResult) {
            String suffix = Joiner.on("").join(value.get(0) % 2, value.get(1) % 2);
            LOGGER.info("doSharding参数信息,计算得到的suffix={}", suffix);
            for (String tableName : availableTargetTableNames) {
                if (tableName.endsWith(suffix)) {
                    realTableNames.add(tableName);
                }
            }
        }
        
        LOGGER.warn("availableTargetTableNames匹配结果为:【{}】", ArrayUtils.toString(realTableNames));
        return realTableNames;
    }
    
    /**
     * 根据类型,判断对应的字段Cloume解析结果
     */
    @SuppressWarnings({ "unchecked" })
    private <T> Set<Integer> getShardingValue(final Collection<ShardingValue<?>> shardingCloumeValues, final String shardingCloume,Class<T> classsType) {
        
        Set<Integer> valueSet = new HashSet<>();
        ShardingValue<?> shardingValue = null;
        
        for (ShardingValue<?> shardingCloumeValue : shardingCloumeValues) {
            if (shardingCloumeValue.getColumnName().equals(shardingCloume)) {
                if(classsType.isAssignableFrom(Integer.class)){
                    shardingValue = (ShardingValue<Integer>) shardingCloumeValue;
                    
                }else if(classsType.isAssignableFrom(String.class)){
                    shardingValue = (ShardingValue<String>) shardingCloumeValue;
                    
                }if(classsType.isAssignableFrom(Date.class)){
                    shardingValue = (ShardingValue<String>) shardingCloumeValue;
                    
                }else{
                    continue;
                }
                //break;
            }
        }
        
        if (null != shardingValue) {
            /*switch (shardingValue.getType()) {
            case SINGLE:
                valueSet.add(shardingValue.getValue());
                break;
            case LIST:
                valueSet.addAll(shardingValue.getValues());
                break;
            case RANGE:
                for (Integer i = shardingValue.getValueRange().lowerEndpoint(); i <= shardingValue.getValueRange().upperEndpoint(); i++) {
                    valueSet.add(i);
                }
                break;
            default:
                throw new UnsupportedOperationException();
            }*/
        }
        
        return valueSet;
    }
    

}
