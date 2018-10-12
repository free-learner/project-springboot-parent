package com.personal.springboot.scheduled.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.common.excel.ExcelWriter;

/**
 * 客户审批表
 * 
 * @Author LiuBao
 * @Version 2.0 2017年07月04日
 */
public class JdbcCustomerBasicInfo extends JdbcBaseConfig{
    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcCustomerBasicInfo.class);

    public static void getCustomerBasicInfo(boolean productFlag,String localFilePath) throws Exception {
        Long begin = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append("lu.mobile_phone, ")
            .append("cbi.custcd, ")
            .append("cbi.user_name, ")
            .append("upc.total_count, ")
            .append("cbi.certify_flag, ")
            .append("uzc.auth_result, ")
            .append("uzc.score_zm, ")
            .append("cbi.create_date ")
            .append(" FROM ")
            .append(" loan_user lu ")
            .append("LEFT JOIN customer_basic_info cbi ON lu. CODE = cbi.user_code ")
            .append("LEFT JOIN user_phone_contacts upc ON lu. CODE = upc.user_code ")
            .append("LEFT JOIN user_zmcredit uzc ON lu. CODE = uzc.user_code ");
        
        String tableName="loan_user";
        String sql =sb.toString();
        String filename = "客户审批表";
        String sheetName = "数据主表";
        String[] titleStrs = { "手机号", "信贷客户号" , "用户名" , "通讯录是否授权" , "通讯录授权条数" , "银行卡四要素验证"  , "芝麻授信标记"  , "芝麻分数"  , "创建时间" };
        @SuppressWarnings("unused")
        int filenameIndex = 0;
        setTotalCount(caculateTotalCount(tableName,productFlag));
        LOGGER.warn("getCustomerBasicInfo执行总数计算耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
        int totalPage = getTotalPage();
        LOGGER.warn("getCustomerBasicInfo开始了.totalPage={}" , totalPage);
        if(totalPage>maxPageSize){
            totalPage=maxPageSize;
        }
        if(totalPage<=0){
            LOGGER.error("getCustomerBasicInfo因为totalPage为{},提前结束..." , getTotalPage());
            return;
        }
        
        List<String[]> contentList = new ArrayList<String[]>();
        List<String> mobilePhoneList = new ArrayList<String>();
        java.sql.Connection conn = getConn(productFlag);
        try {
            conn.setAutoCommit(true);
            PreparedStatement pst = conn.prepareStatement("");
            for (int pageIndex = 0; pageIndex < totalPage; pageIndex++) {
                int startIndex = pageIndex * batchSize;
                int pageSize = batchSize;
                LOGGER.warn("getCustomerBasicInfo:pageIndex为:{},pageSize为:{}" , pageIndex,pageSize);
                String sqlSuffix = " LIMIT " + startIndex + ", " + pageSize;
                LOGGER.warn("getCustomerBasicInfo最终查询sql为:"+(sql+sqlSuffix));
                ResultSet executeQuery = pst.executeQuery(sql+sqlSuffix);
                while (executeQuery.next()) {
                    String[] param=new String[titleStrs.length];
                    param[0] = executeQuery.getString("mobile_phone");
                    if(mobilePhoneList.contains(param[0])){
                        LOGGER.warn("执行了一次手机号码过滤操作!");
                        continue;
                    }
                    mobilePhoneList.add(param[0]);
                    param[1] = executeQuery.getString("custcd");
                    param[2] = executeQuery.getString("user_name");
                    String total_count = executeQuery.getString("total_count");
                    if(StringUtils.isBlank(total_count)){
                        param[3] = "";
                        param[4] = "";
                    }else{
                        int totalCount = Integer.valueOf(total_count);
                        if(totalCount<=-1){
                            param[3] = "未授权";
                        }else{
                            param[3] = "授权";
                        }
                        if(totalCount<=0){
                            param[4] = "0";
                        }else{
                            param[4] = total_count;
                        }
                    }
                    param[5] = executeQuery.getString("certify_flag");
                    param[6] = executeQuery.getString("auth_result");
                    param[7] = executeQuery.getString("score_zm");
                    param[8] = executeQuery.getString("create_date");
                    if(ArrayUtils.isNotEmpty(param)){
                        contentList.add(param);
                    }
                }
                if(pageIndex>0&&pageIndex%batchSize==0&&CollectionUtils.isNotEmpty(contentList)){
                    //ExcelWriter.writeExcel2007(titleStrs, contentList, filename+filenameIndex++, sheetName);
                    //contentList.clear();
                    LOGGER.warn("getCustomerBasicInfo执行了一次刷新数据..." );
                    Thread.sleep(200);
                }
            }
            if(CollectionUtils.isNotEmpty(contentList)){
                ExcelWriter.writeExcel2007(titleStrs, contentList, filename, sheetName,localFilePath);
                LOGGER.warn("getCustomerBasicInfo执行了最后一次刷新数据..." );
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("getCustomerBasicInfo查询数据库信息异常", e);
        }
        LOGGER.warn("getCustomerBasicInfo执行结束,总共耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
    }

}
