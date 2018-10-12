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
import com.personal.springboot.common.utils.DatetimeUtils;

/**
 * 设备授权信息
 * 
 * @Author LiuBao
 * @Version 2.0 2017年07月04日
 */
public class JdbcUserOperationHistory extends JdbcBaseConfig{
    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcUserOperationHistory.class);

    public static void getUserOperationHistory(boolean productFlag,String localFilePath) throws Exception {
        Long begin = System.currentTimeMillis();
      //开始时间为前天零时
        String beginTimestamp = DatetimeUtils.dateTimeZero(DatetimeUtils.dayPlus(DatetimeUtils.currentTimestamp(),-1));
        //结束时间为当天零时
        String endTimestamp = DatetimeUtils.dateTimeZero();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ")
            .append("lu.mobile_phone, ")
            .append("uoh.ip_address, ")
            .append("uoh.longitude, ")
            .append("uoh.latitude, ")
            .append("uoh.device_id, ")
            .append("uoh.device_info, ")
            .append("uoh.device_version, ")
            .append("uoh.plateform, ")
            .append("uoh.imei, ")
            .append("uoh.imsi, ")
            .append("uoh.wifi_mac, ")
            .append("uoh.installed_list, ")
            .append("uoh.gyroscopex, ")
            .append("uoh.gyroscopey, ")
            .append("uoh.gyroscopez, ")
            .append("uoh.battery_capacity, ")
            .append("uoh.opt_type, ")
            .append("uoh.create_date ")
            .append(" FROM ")
            .append(" user_operation_history uoh ")
            .append("INNER JOIN loan_user lu on uoh.user_code is NOT NULL ")
            .append("AND uoh.user_code=lu.CODE ")
            .append(" and uoh.create_date BETWEEN '")
            .append(beginTimestamp)
            .append("' AND '")
            .append(endTimestamp)
            .append("' ")
            .append(" ORDER BY uoh.create_date");
        
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" user_operation_history uoh ")
                .append(" INNER JOIN loan_user lu on uoh.user_code is NOT NULL ")
                .append(" AND uoh.user_code=lu.CODE ")
                .append(" and uoh.create_date BETWEEN '")
                .append(beginTimestamp)
                .append("' AND '")
                .append(endTimestamp)
                .append("' ");
        String tableName=sb2.toString();
        //String tableName="user_operation_history";
        String sql =sb.toString();
        String filename = "设备授权信息";
        String sheetName = "数据主表";
        String[] titleStrs = { "用户手机号", "IP地址" , "经度" , "纬度" ,"设备ID","设备信息","设备版本","设备类型","IMEI","IMSI","WifiMac地址","应用安装列表","陀螺仪X","陀螺仪Y","陀螺仪Z","电量","操作类型","访问时间"};
        @SuppressWarnings("unused")
        int filenameIndex = 0;
        setTotalCount(caculateTotalCount(tableName,productFlag));
        LOGGER.warn("getUserOperationHistory执行总数计算耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
        int totalPage = getTotalPage();
        LOGGER.warn("getUserOperationHistory开始了.totalPage={}" , totalPage);
        if(totalPage>maxPageSize){
            totalPage=maxPageSize;
        }
        if(totalPage<=0){
            LOGGER.error("getUserOperationHistory因为totalPage为{},提前结束..." , totalPage);
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
                LOGGER.warn("getUserOperationHistory :pageIndex为:{},pageSize为:{}" , pageIndex,pageSize);
                String sqlSuffix = " LIMIT " + startIndex + ", " + pageSize;
                LOGGER.warn("getUserOperationHistory最终查询sql为:"+(sql+sqlSuffix));
                ResultSet executeQuery = pst.executeQuery(sql+sqlSuffix);
                while (executeQuery.next()) {
                    String[] param=new String[titleStrs.length];
                    param[0] = executeQuery.getString("mobile_phone");
                    param[1] = executeQuery.getString("ip_address");
                    param[2] = executeQuery.getString("longitude");
                    param[3] = executeQuery.getString("latitude");
                    param[4] = executeQuery.getString("device_id");
                    param[5] = executeQuery.getString("device_info");
                    param[6] = executeQuery.getString("device_version");
                    param[7] = executeQuery.getString("plateform");
                    param[8] = executeQuery.getString("imei");
                    param[9] = executeQuery.getString("imsi");
                    param[10] = executeQuery.getString("wifi_mac");
                    param[11] = executeQuery.getString("installed_list");
                    param[12] = executeQuery.getString("gyroscopex");
                    param[13] = executeQuery.getString("gyroscopey");
                    param[14] = executeQuery.getString("gyroscopez");
                    param[15] = executeQuery.getString("battery_capacity");
                    param[17] = executeQuery.getString("create_date");
//                    param[16] = executeQuery.getString("opt_type");
                    String opt_type = executeQuery.getString("opt_type");
                    if(StringUtils.isNoneBlank(opt_type)){
                        switch (opt_type) {
                        case "register":
                            param[16] = "注册";
                            break;
                        case "loginpwd":
                            param[16] = "密码登录";
                            break;
                        case "loginsms":
                            param[16] = "短信快捷登录";
                            break;
                        case "logout":
                            param[16] = "退出登录";
                            break;
                        case "resetpwd":
                            param[16] = "重置密码";
                            break;
                        case "cardAuth":
                            param[16] = "个人信息绑定银行卡";
                            break;
                        case "userContactsAuth":
                            param[16] = "添加联系人";
                            break;
                        case "backgroundAuth":
                            param[16] = "添加背景信息";
                            break;
                        case "creditCardAuth":
                            param[16] = "信用卡信息添加";
                            break;
                        case "zmAuth":
                            param[16] = "芝麻授信";
                            break;
                        case "indexDetail":
                            param[16] = "首页产品信息查询";
                            break;
                        case "serveDetail":
                            param[16] = "服务详情信息查询";
                            break;
                        case "messageListByPage":
                            param[16] = "推送消息列表查询";
                            break;
                        case "listCouponByPage":
                            param[16] = "卡券列表信息查询";
                            break;
                        case "listLoanByPage":
                            param[16] = "借条列表信息查询";
                            break;
                        case "loanDetail":
                            param[16] = "借条详情信息查询";
                            break;
                        case "listBillByPage":
                            param[16] = "账单列表信息查询";
                            break;
                        case "contractDetail":
                            param[16] = "合同信息查看";
                            break;
                        case "loanCommit":
                            param[16] = "提交借据";
                            break;
                        case "repaymentCommit":
                            param[16] = "提交还款申请";
                            break;
                        case "listCurrentVersion":
                            param[16] = "版本升级信息查询";
                            break;
                        case "loanUpload":
                            param[16] = "借据还款凭证信息上传";
                            break;
                        case "allDetail":
                            param[16] = "我的页面";
                            break;
                        case "detailByCode":
                            param[16] = "消息详情";
                            break;
                        case "creditCardDetail":
                            param[16] = "信用卡详情";
                            break;
                        case "userContactsDetail":
                            param[16] = "联系人详情";
                            break;
                        case "cardDetail":
                            param[16] = "银行卡详情";
                            break;
                        case "backgroundDetail":
                            param[16] = "背景详情";
                            break;
                        case "zmcreditDetail":
                            param[16] = "芝麻详情";
                            break;
                        default:
                            param[16] = "未知";
                            break;
                        }
                    } else {
                        param[16] = "";
                    }
                    
                    if(ArrayUtils.isNotEmpty(param)){
                        contentList.add(param);
                    }
                }
                if(pageIndex>0&&pageIndex%batchSize==0&&CollectionUtils.isNotEmpty(contentList)){
                    //ExcelWriter.writeExcel2007(titleStrs, contentList, filename+filenameIndex++, sheetName);
                    //contentList.clear();
                    LOGGER.warn("getUserOperationHistory执行了一次刷新数据..." );
                    Thread.sleep(200);
                }
            }
            if(CollectionUtils.isNotEmpty(contentList)){
                ExcelWriter.writeExcel2007(titleStrs, contentList, filename, sheetName,localFilePath);
                LOGGER.warn("getUserOperationHistory执行了最后一次刷新数据..." );
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("getUserOperationHistory查询数据库信息异常", e);
        }
        LOGGER.warn("getUserOperationHistory执行结束,总共耗时 : {} S" , (System.currentTimeMillis() - begin) / 1000);
    }

}
