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
 * 信用卡信息列表
 * 
 * @Author LiuBao
 * @Version 2.0 2017年07月04日
 */
public class JdbcUserCreditCard extends JdbcBaseConfig{
    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcUserCreditCard.class);

    public static void getUserCreditCard(boolean productFlag,String localFilePath) throws Exception {
        Long begin = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append("cbi.mobile_phone, ")
            .append("cbi.user_name, ")
            .append("cbi.id_no, ")
            .append("ucc.bank_name, ")
            .append("ucc.card_number, ")
            .append("ucc.card_limit, ")
            .append("ucc.card_level ")
            .append(" FROM ")
            .append(" user_credit_card ucc ")
            .append("LEFT JOIN customer_basic_info cbi ON ucc.user_code = cbi.user_code");
        
        String tableName="user_credit_card";
        String sql =sb.toString();
        String filename = "信用卡信息表";
        String sheetName = "数据主表";
        String[] titleStrs = { "手机号", "姓名", "身份证" , "银行名称" , "信用卡号" , "额度" , "级别" };
        @SuppressWarnings("unused")
        int filenameIndex = 0;
        setTotalCount(caculateTotalCount(tableName,productFlag));
        LOGGER.warn("getUserCreditCard执行总数计算耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
        int totalPage = getTotalPage();
        LOGGER.warn("getUserCreditCard开始了.totalPage={}" , totalPage);
        if(totalPage>maxPageSize){
            totalPage=maxPageSize;
        }
        if(totalPage<=0){
            LOGGER.error("getUserCreditCard因为totalPage为{},提前结束..." , getTotalPage());
            return;
        }
        
        List<String[]> contentList = new ArrayList<String[]>();
        java.sql.Connection conn = getConn(productFlag);
        try {
            conn.setAutoCommit(true);
            PreparedStatement pst = conn.prepareStatement("");
            for (int pageIndex = 0; pageIndex < totalPage; pageIndex++) {
                int startIndex = pageIndex * batchSize;
                int pageSize = batchSize;
                LOGGER.warn("getUserCreditCard :pageIndex为:{},pageSize为:{}" , pageIndex,pageSize);
                String sqlSuffix = " LIMIT " + startIndex + ", " + pageSize;
                LOGGER.warn("getUserCreditCard最终查询sql为:"+(sql+sqlSuffix));
                ResultSet executeQuery = pst.executeQuery(sql+sqlSuffix);
                while (executeQuery.next()) {
                    String[] param=new String[titleStrs.length];
                    param[0] = executeQuery.getString("mobile_phone");
                    param[1] = executeQuery.getString("user_name");
                    param[2] = executeQuery.getString("id_no");
                    param[3] = executeQuery.getString("bank_name");
                    param[4] = executeQuery.getString("card_number");
                    param[5] = executeQuery.getString("card_limit");
                    String card_level = executeQuery.getString("card_level");
                    if(StringUtils.isNoneBlank(card_level)){
                        switch (card_level) {
                        case "1.0":
                            param[6] = "公务卡";
                            break;
                        case "2.0":
                            param[6] = "普卡";
                            break;
                        case "3.0":
                            param[6] = "金卡";
                            break;
                        case "4.0":
                            param[6] = "白金卡";
                            break;
                        case "4.1":
                            param[6] = "钛金卡";
                            break;
                        case "4.2":
                            param[6] = "钻石卡";
                            break;
                        case "4.3":
                            param[6] = "无限卡";
                            break;
                        default:
                            param[6] = "未知";
                            break;
                        }
                    }else{
                        param[6] = "未知#";
                    }
                    if(ArrayUtils.isNotEmpty(param)){
                        contentList.add(param);
                    }
                }
                if(pageIndex>0&&pageIndex%batchSize==0&&CollectionUtils.isNotEmpty(contentList)){
                    //ExcelWriter.writeExcel2007(titleStrs, contentList, filename+filenameIndex++, sheetName);
                    //contentList.clear();
                    LOGGER.warn("getUserCreditCard执行了一次刷新数据..." );
                    Thread.sleep(200);
                }
            }
            if(CollectionUtils.isNotEmpty(contentList)){
                ExcelWriter.writeExcel2007(titleStrs, contentList, filename, sheetName,localFilePath);
                LOGGER.warn("getUserCreditCard执行了最后一次刷新数据..." );
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("getUserCreditCard查询数据库信息异常", e);
        }
        LOGGER.warn("getUserCreditCard执行结束,总共耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
    }

}
