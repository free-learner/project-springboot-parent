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
 * 客户信息表
 * 
 * @Author LiuBao
 * @Version 2.0 2017年07月04日
 */
public class JdbcLoanUser extends JdbcBaseConfig{
    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcLoanUser.class);

    public static void getLoanUser(boolean productFlag,String localFilePath) throws Exception {
        Long begin = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append("lu.mobile_phone, ")
            .append("cbi.custcd, ")
            .append("cbi.user_name, ")
            .append("cbi.id_no, ")
            .append("cbi.act_chn_name, ")
            .append("cbi.pay_no, ")
            .append("cbi.reserve_tel, ")
            .append("cbi.channel_id, ")
            .append("cbi.user_type, ")
            .append("cbi.create_date, ")
            .append("CONCAT(uq.user_privince,uq.user_area,uq.user_street) AS user_city, ")
            .append("uq.user_address, ")
            .append("uq.educate_situation, ")
            .append("uq.if_marry, ")
            .append("uq.company_name, ")
            .append("CONCAT(uq.company_privince,uq.company_area,uq.company_street) AS company_city, ")
            .append("uq.company_address, ")
            .append("uq.job_type, ")
            .append("uq.position_type, ")
            .append("uq.annual_income ")
            .append(" FROM ")
            .append(" loan_user lu ")
            .append("LEFT JOIN customer_basic_info cbi ON lu. CODE = cbi.user_code ")
            .append("LEFT JOIN user_qualifications uq ON lu. CODE = uq.user_code ");
        
        String tableName="loan_user";
        String sql =sb.toString();
        String filename = "客户信息表";
        String sheetName = "数据主表";
        String[] titleStrs = { "手机号", "信贷客户号" , "用户名" , "身份证" ,"银行名称","银行卡号","银行预留手机号","是否会员","会员标签","创建时间","个人城市","详细地址","教育程度","婚否","公司名称","公司城市","公司地址","工作类型","工作职务","年收入(万)"};
        @SuppressWarnings("unused")
        int filenameIndex = 0;
        setTotalCount(caculateTotalCount(tableName,productFlag));
        LOGGER.warn("getLoanUser执行总数计算耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
        int totalPage = getTotalPage();
        LOGGER.warn("getLoanUser开始了.totalPage={}" , totalPage);
        if(totalPage>maxPageSize){
            totalPage=maxPageSize;
        }
        if(totalPage<=0){
            LOGGER.error("getLoanUser因为totalPage为{},提前结束..." , getTotalPage());
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
                LOGGER.warn("getLoanUser:pageIndex为:{},pageSize为:{}" , pageIndex,pageSize);
                String sqlSuffix = " LIMIT " + startIndex + ", " + pageSize;
                LOGGER.warn("getLoanUser最终查询sql为:"+(sql+sqlSuffix));
                ResultSet executeQuery = pst.executeQuery(sql+sqlSuffix);
                while (executeQuery.next()) {
                    String[] param=new String[titleStrs.length];
                    param[0] = executeQuery.getString("mobile_phone");
                    if(mobilePhoneList.contains(param[0])){
                        LOGGER.warn("执行了一次手机号码过滤操作!");
                        continue;
                    }
                    mobilePhoneList.add(param[0]);
                    param[1] = executeQuery.getString("cbi.custcd");
                    param[2] = executeQuery.getString("user_name");
                    String id_no = executeQuery.getString("id_no");
                    param[3] = id_no;
                    param[4] = executeQuery.getString("act_chn_name");
                    param[5] = executeQuery.getString("pay_no");
                    param[6] = executeQuery.getString("reserve_tel");
                    String channel_id = executeQuery.getString("channel_id");
                    if(StringUtils.isNoneBlank(channel_id)){
                        switch (channel_id) {
                        case "1":
                            param[7] = "会员";
                            break;
                        default:
                            if(StringUtils.isNoneBlank(id_no)){
                                param[7] = "非会员";
                            }else{
                                param[7] = "";
                            }
                            break;
                        }
                    }else{
                        param[7] = "";
                    }
                    param[8] = executeQuery.getString("user_type");
                    param[9] = executeQuery.getString("create_date");
                    param[10] = executeQuery.getString("user_city");
                    param[11] = executeQuery.getString("user_address");
                    String educate_situation = executeQuery.getString("educate_situation");
                    if(StringUtils.isNoneBlank(educate_situation)){
                        switch (educate_situation) {
                        case "200":
                            param[12] = "小学";
                            break;
                        case "201":
                            param[12] = "初中";
                            break;
                        case "202":
                            param[12] = "高中/中专";
                            break;
                        case "203":
                            param[12] = "大专";
                            break;
                        case "204":
                            param[12] = "本科";
                            break;
                        case "205":
                            param[12] = "硕士及以上";
                            break;
                        default:
                            param[12] = "未知";
                            break;
                        }
                    }else{
                        param[12] = "";
                    }
                    String if_marry = executeQuery.getString("if_marry");
                    if(StringUtils.isNoneBlank(if_marry)){
                        switch (if_marry) {
                        case "300":
                            param[13]="未婚";
                            break;
                        case "301":
                            param[13]="已婚未育";
                            break;
                        case "302":
                            param[13]="已婚已育";
                            break;
                        case "303":
                            param[13]="离异";
                            break;
                        case "304":
                            param[13]="其他";
                            break;
                        default:
                            param[13]="未知";
                            break;
                        }
                    }else{
                        param[13]="";
                    }
                    param[14] = executeQuery.getString("company_name");
                    param[15] = executeQuery.getString("company_city");
                    param[16] = executeQuery.getString("company_address");
                    param[17] = executeQuery.getString("job_type");
                    param[18] = executeQuery.getString("position_type");
                    param[19] = executeQuery.getString("annual_income");
                    if(ArrayUtils.isNotEmpty(param)){
                        contentList.add(param);
                    }
                }
                if(pageIndex>0&&pageIndex%batchSize==0&&CollectionUtils.isNotEmpty(contentList)){
                    //ExcelWriter.writeExcel2007(titleStrs, contentList, filename+filenameIndex++, sheetName);
                    //contentList.clear();
                    LOGGER.warn("getLoanUser执行了一次刷新数据..." );
                    Thread.sleep(200);
                }
            }
            if(CollectionUtils.isNotEmpty(contentList)){
                ExcelWriter.writeExcel2007(titleStrs, contentList, filename, sheetName,localFilePath);
                LOGGER.warn("getLoanUser执行了最后一次刷新数据..." );
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("getLoanUser查询数据库信息异常", e);
        }
        LOGGER.warn("getLoanUser执行结束,总共耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
    }

}
