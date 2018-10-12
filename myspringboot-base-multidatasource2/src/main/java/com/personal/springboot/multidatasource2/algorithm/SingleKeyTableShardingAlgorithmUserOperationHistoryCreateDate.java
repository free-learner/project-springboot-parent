package com.personal.springboot.multidatasource2.algorithm;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.DatetimeUtils;
/**
 *  UserOperationHistory根据CreateDate分表的逻辑函数
 *  根据CreateDate<Date>字段进行对应的分表<查询>操作
 *  
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月15日
 */
public class SingleKeyTableShardingAlgorithmUserOperationHistoryCreateDate extends HashCodeSingleStringKeyAlgorithm implements SingleKeyTableShardingAlgorithm<Timestamp>{
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleKeyTableShardingAlgorithmUserOperationHistoryCreateDate.class);
    
    /**
     * sql 中 = 操作时，table的映射
     */
    @Override
    public String doEqualSharding(final Collection<String> availabletableNames, final ShardingValue<Timestamp> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doInSharding参数信息:availabletableNames={}",ArrayUtils.toString(availabletableNames));
            LOGGER.info("doInSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        
        String logicTableName = shardingCloumeValue.getLogicTableName();
        String columnName = shardingCloumeValue.getColumnName();
        Date createDate = null;
        if ("create_date".equals(columnName)) {
            createDate = shardingCloumeValue.getValue();
        }
        
        //按照日期進行分表查詢[没有插入]
        String formatDate = DatetimeUtils.formatDate(createDate, "yyyyMM");
        String realTableName = logicTableName +  "_" + formatDate;
        LOGGER.warn("availabletableNames匹配结果为:【{}】",realTableName);
        if(availabletableNames.contains(realTableName)){
            return realTableName;
        }
        throw new IllegalArgumentException("表规则信息为找到,匹配失败!");
    }

    /**
     * sql 中 in 操作时，table的映射
     */
    @Override
    public Collection<String> doInSharding(final Collection<String> availabletableNames, final ShardingValue<Timestamp> shardingCloumeValue) {
        LOGGER.error("【doInSharding暂未实现】");
        return null;
    }

    /**
     * sql 中 between 操作时，table的映射
     */
    @Override
    public Collection<String> doBetweenSharding(final Collection<String> availabletableNames, final ShardingValue<Timestamp> shardingCloumeValue) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("doBetweenSharding参数信息:availabletableNames={}",ArrayUtils.toString(availabletableNames));
            LOGGER.info("doBetweenSharding参数信息:shardingCloumeValue={}",shardingCloumeValue.toString());
        }
        
        int size = availabletableNames.size();
        String logicTableName = shardingCloumeValue.getLogicTableName();
        String columnName = shardingCloumeValue.getColumnName();
        //去重复元素集合
        Collection<String> resultList = new LinkedHashSet<String>(size);
        
        Date createDate = null;
        if ("create_date".equals(columnName)) {
            createDate = shardingCloumeValue.getValue();
        }
        Assert.notNull(createDate,"获取的createDate值为空!");
        
        Range<Timestamp> range = shardingCloumeValue.getValueRange();
        Timestamp lowerEndpoint = range.lowerEndpoint();
        Timestamp upperEndpoint = range.upperEndpoint();
        Collection<String> monthBetween = getYearMonthBetween(lowerEndpoint, upperEndpoint);
        for (String month : monthBetween) {
            //按照日期進行分表查詢[没有插入]
            String realTableName = logicTableName +  "_" + month;
            LOGGER.warn("realTableName为:【{}】",realTableName);
            if(availabletableNames.contains(realTableName)){
                resultList.add(realTableName);
            }
            
            /*for (String tableName : availabletableNames) {
                if(tableName.endsWith(month)){
                    resultList.add(tableName);  
                }
            }*/
            
        }
        
        /*Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(lowerEndpoint);
        max.setTime(upperEndpoint);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("日期筛选lowerEndpoint[{}]",DateTimeUtil.formatDate2Str(lowerEndpoint));
            LOGGER.info("日期筛选upperEndpoint[{}]",DateTimeUtil.formatDate2Str(upperEndpoint));
        }
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 1);
        
        while (min.before(max)) {
            String realTableName = logicTableName +  "_" + getYearMonth(min.getTime());
            LOGGER.warn("realTableName为:【{}】",realTableName);
            if(availabletableNames.contains(realTableName)){
                resultList.add(realTableName);
            }
            min.add(Calendar.MONTH, 1);
        }*/
        
        return resultList;
    }
    
    private static String getYearMonth(final Date date){
        String yearMonth=DateTimeUtil.formatDate2Str(date, DateTimeUtil.DATE_PATTON_4);
        return yearMonth;
    }
    
    private static Collection<String> getYearMonthBetween(Date minDate ,Date maxDate) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("日期筛选minDate[{}]",DateTimeUtil.formatDate2Str(minDate));
            LOGGER.info("日期筛选maxDate[{}]",DateTimeUtil.formatDate2Str(maxDate));
        }
        Collection<String> resultList = new LinkedHashSet<String>();
        if(minDate.after(maxDate)){
            //日期交换
            Date tmp=minDate;
            minDate=maxDate;
            maxDate=tmp;
        }
        
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        while (min.before(max)) {
            resultList.add(getYearMonth(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return resultList;
    }

}
