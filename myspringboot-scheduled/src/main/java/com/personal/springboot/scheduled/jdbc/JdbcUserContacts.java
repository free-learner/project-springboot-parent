package com.personal.springboot.scheduled.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.common.excel.ExcelWriter;

/**
 * 客户信息表-联系人
 * 
 * @Author LiuBao
 * @Version 2.0 2017年07月04日
 */
public class JdbcUserContacts extends JdbcBaseConfig{
    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcUserContacts.class);

    public static void getUserContacts1(boolean productFlag,String localFilePath) throws Exception {
        Long begin = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append("lu.mobile_phone, ")
            .append("uc.contact_name, ")
            .append("uc.contact_relation, ")
            .append("uc.mobile_phone as 'mobile_phone2' ")
            .append(" FROM ")
            .append(" user_contacts uc LEFT JOIN loan_user lu ")
            .append(" ON lu. CODE = uc.user_code ")
            .append("WHERE uc.contact_type='1' ");
        
        String tableName=" user_contacts uc WHERE uc.contact_type='1' ";
        String sql =sb.toString();
        String filename = "客户信息表-联系人1";
        String sheetName = "user_contacts1";
        String[] titleStrs = { "用户手机号", "联系人姓名" , "联系人关系" , "联系人电话" };
        @SuppressWarnings("unused")
        int filenameIndex = 0;
        setTotalCount(caculateTotalCount(tableName,productFlag));
        LOGGER.warn("getUserContacts1执行总数计算耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
        int totalPage = getTotalPage();
        LOGGER.warn("getUserContacts1开始了.totalPage={}" , totalPage);
        if(totalPage>maxPageSize){
            totalPage=maxPageSize;
        }
        if(totalPage<=0){
            LOGGER.error("getUserContacts1因为totalPage为{},提前结束..." , getTotalPage());
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
                LOGGER.warn("getUserContacts1 :pageIndex为:{},pageSize为:{}" , pageIndex,pageSize);
                String sqlSuffix = " LIMIT " + startIndex + ", " + pageSize;
                LOGGER.warn("getUserContacts1最终查询sql为:"+(sql+sqlSuffix));
                ResultSet executeQuery = pst.executeQuery(sql+sqlSuffix);
                while (executeQuery.next()) {
                    String[] param=new String[titleStrs.length];
                    param[0] = executeQuery.getString("mobile_phone");
                    param[1] = executeQuery.getString("contact_name");
                    param[2] = executeQuery.getString("contact_relation");
                    param[3] = executeQuery.getString("mobile_phone2");
                    if(ArrayUtils.isNotEmpty(param)){
                        contentList.add(param);
                    }
                }
                if(pageIndex>0&&pageIndex%batchSize==0&&CollectionUtils.isNotEmpty(contentList)){
                    //ExcelWriter.writeExcel2007(titleStrs, contentList, filename+filenameIndex++, sheetName);
                    //contentList.clear();
                    LOGGER.warn("getUserContacts1执行了一次刷新数据..." );
                    Thread.sleep(200);
                }
            }
            if(CollectionUtils.isNotEmpty(contentList)){
                ExcelWriter.writeExcel2007(titleStrs, contentList, filename, sheetName,localFilePath);
                LOGGER.warn("getUserContacts1执行了最后一次刷新数据..." );
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("getUserContacts1查询数据库信息异常", e);
        }
        LOGGER.warn("getUserContacts1执行结束,总共耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
    }
    
    public static void getUserContacts2(boolean productFlag,String localFilePath) throws Exception {
        Long begin = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
        .append("lu.mobile_phone, ")
        .append("uc.contact_name, ")
        .append("uc.contact_relation, ")
        .append("uc.mobile_phone as 'mobile_phone2' ")
        .append(" FROM ")
        .append(" user_contacts uc LEFT JOIN loan_user lu ")
        .append(" ON lu. CODE = uc.user_code ")
        .append("WHERE uc.contact_type='2' ");
        
        String tableName=" user_contacts uc WHERE uc.contact_type='2' ";
        String sql =sb.toString();
        String filename = "客户信息表-联系人2";
        String sheetName = "user_contacts2";
        String[] titleStrs = { "用户手机号", "联系人姓名" , "联系人关系" , "联系人电话" };
        @SuppressWarnings("unused")
        int filenameIndex = 0;
        setTotalCount(caculateTotalCount(tableName,productFlag));
        LOGGER.warn("getUserContacts2执行总数计算耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
        int totalPage = getTotalPage();
        LOGGER.warn("getUserContacts2开始了.totalPage={}" , totalPage);
        if(totalPage>maxPageSize){
            totalPage=maxPageSize;
        }
        if(totalPage<=0){
            LOGGER.error("getUserContacts2因为totalPage为{},提前结束..." , getTotalPage());
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
                LOGGER.warn("getUserContacts2 :pageIndex为:{},pageSize为:{}" , pageIndex,pageSize);
                String sqlSuffix = " LIMIT " + startIndex + ", " + pageSize;
                LOGGER.warn("getUserContacts2最终查询sql为:"+(sql+sqlSuffix));
                ResultSet executeQuery = pst.executeQuery(sql+sqlSuffix);
                while (executeQuery.next()) {
                    String[] param=new String[titleStrs.length];
                    param[0] = executeQuery.getString("mobile_phone");
                    param[1] = executeQuery.getString("contact_name");
                    param[2] = executeQuery.getString("contact_relation");
                    param[3] = executeQuery.getString("mobile_phone2");
                    if(ArrayUtils.isNotEmpty(param)){
                        contentList.add(param);
                    }
                }
                if(pageIndex>0&&pageIndex%batchSize==0&&CollectionUtils.isNotEmpty(contentList)){
                    //ExcelWriter.writeExcel2007(titleStrs, contentList, filename+filenameIndex++, sheetName);
                    //contentList.clear();
                    LOGGER.warn("getUserContacts2执行了一次刷新数据..." );
                    Thread.sleep(200);
                }
            }
            if(CollectionUtils.isNotEmpty(contentList)){
                ExcelWriter.writeExcel2007(titleStrs, contentList, filename, sheetName,localFilePath);
                LOGGER.warn("getUserContacts2执行了最后一次刷新数据..." );
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("getUserContacts2查询数据库信息异常", e);
        }
        LOGGER.warn("getUserContacts2执行结束,总共耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
    }

}
