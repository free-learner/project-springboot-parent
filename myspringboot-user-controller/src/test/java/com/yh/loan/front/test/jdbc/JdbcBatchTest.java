package com.yh.loan.front.test.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.excel.ExcelWriter;
import com.personal.springboot.common.utils.DESEncryptUtil;
import com.personal.springboot.common.utils.idcard.IdcardUtils;
import com.personal.springboot.service.RedisCacheService;
import com.personal.springboot.user.controller.task.RedisCacheAsyncTask;
import com.yh.loan.front.test.base.BaseServicesTest;


/**
 * 
 * 存储函数:
drop function if exists batch_insert_test;
DELIMITER &&
CREATE  FUNCTION  batch_insert_test (param_i INT )  
RETURNS VARCHAR(20)  
BEGIN  
declare num int;  
SET num=1 ;
WHILE num <= param_i 
do
SET num=num+1;
insert into tb_big_data(count, create_time, random) values(param_i,SYSDATE(),UUID());
end while; 
RETURN  num;  
END&&  
DELIMITER ;

//调用mysql存储过程
SELECT batch_insert_test(100);

存储过程:
drop procedure if exists batch_insert_test;
create procedure batch_insert_test(param_i int)
begin
declare num int;  
SET num=1 ;
WHILE num <= param_i 
do
SET num=num+1;
insert into tb_big_data(count, create_time, random) values(param_i,SYSDATE(),UUID());
end while; 
end

//调用mysql存储过程
call batch_insert_test(100);

 * @Author  LiuBao
 * @Version 2.0
 *   2017年6月19日
 */
@SuppressWarnings("unused")
public class JdbcBatchTest extends BaseServicesTest{
    
    public final static Logger logger = LoggerFactory.getLogger(JdbcBatchTest.class);  
    
    private static final int batchSize=1000*10;
    private static final int totalCount=11628901;
    //private static final int totalPage=totalCount%batchSize==0?totalCount/batchSize:totalCount/batchSize+1;
    private static final int totalPage;//11629
    static{
        totalPage=totalCount%batchSize==0?(totalCount/batchSize) : totalCount/batchSize+1;
        logger.info("當前totalPage的值为:{}",totalPage);
    }
    
    @Autowired
    private RedisCacheService redisCacheService;
    
    @Autowired
    private RedisCacheAsyncTask redisCacheAsyncTask;
    
    @Value("${redis.cache.connectionTimeout}")
    private int connectionTimeout;
    
    /**
     * 100* 60 S ==100*10*10000
     * 100* 60 S ==100*10*10000
     * 50*121 S  ==50*20*10000
     * 20*306 S  ==20*50*10000
     * 10*  619 S ==10*100*10000
     */
    @Test
    public void testBatchInsertFromMysqlThread() throws Exception {
        logger.warn("测试开始了.totalPage="+totalPage+",connectionTimeout="+connectionTimeout);
        // 开时时间
        Long begin =System.currentTimeMillis() ;
        java.sql.Connection conn = getConn();
        try { 
            // 设置事务为非自动提交
            conn.setAutoCommit(true);
            // Statement st = conn.createStatement();
            // 比起st，pst会更好些
            PreparedStatement pst = conn.prepareStatement("");
            Map<String, Object> paramMap=new HashMap<String, Object>();
            // 外层循环，总提交事务次数
            for (int pageIndex= 0; pageIndex < 100/*totalPage*/; pageIndex++) {
                int startIndex=pageIndex*batchSize;
                int pageSize=batchSize;
                String sql = "SELECT * FROM yh_member LIMIT "+startIndex+", "+pageSize;
                logger.warn("sql为:"+sql);
                paramMap.clear();
                ResultSet executeQuery = pst.executeQuery(sql);
                while(executeQuery.next()){
                    String userID = executeQuery.getString("userID");
                    paramMap.put(userID,userID);
                }
                logger.warn("pageIndex为:"+pageIndex);
                redisCacheAsyncTask.executeBatchSaveCache(pageIndex,new HashMap<>(paramMap));
                sql =null;
                if(pageIndex!=0&&pageIndex%10==0){
                    Thread.sleep(connectionTimeout);
                    logger.warn("第{}次休眠结束!",pageIndex);
                }
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //Thread.sleep(60000);
        logger.warn("开始进入休眠状态等待60S..." );
        Thread.sleep(connectionTimeout);
        logger.warn("cast耗时 : {}" , (System.currentTimeMillis() - begin)/1000  + " S");
        logger.warn("测试结束了.totalPage="+totalPage);
    }
    
    /**
     * 测试hr数据表信息
     */
    @Test
    public void testBatchInsertFromMysqlHR() throws Exception {
        logger.warn("测试开始了.totalPage="+totalPage);
        // 开时时间
        Long begin =System.currentTimeMillis() ;
        List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();
        List<String[]> contentList=new ArrayList<>();
        java.sql.Connection conn = getConn();
        try {
            // 设置事务为非自动提交
            conn.setAutoCommit(true);
            // Statement st = conn.createStatement();
            // 比起st，pst会更好些
            PreparedStatement pst = conn.prepareStatement("");
            // 外层循环，总提交事务次数
            for (int pageIndex= 0; pageIndex < 1; pageIndex++) {
                //int pageIndex=0;
                int startIndex=pageIndex;
                int pageSize=batchSize;
                // sql前缀
                String sql = "SELECT cust_name,id_no,tel_number,current_salary,bir_date,section,current_job,bir_address,educate_degree,employee_date,last_syn_time FROM customer_basic_info_hr LIMIT "+startIndex+", "+pageSize;
                logger.warn("sql为:"+sql);
                // 添加执行sql
                ResultSet executeQuery = pst.executeQuery(sql);
                while(executeQuery.next()){
                    Map<String, String> paramMap=new LinkedHashMap<String, String>();
                    String[] paramArray=new String[11];
                    paramMap.put("name",executeQuery.getString("cust_name"));
                    paramMap.put("job",executeQuery.getString("current_job"));
                    /*if(!StringUtils.equalsAny( executeQuery.getString("current_job"),"核心合伙人","联合创始人","战略合伙人")){
                        continue;
                    }*/
                    String currentSalary = executeQuery.getString("current_salary");
                    if(StringUtils.isNoneBlank(currentSalary)){
                        paramMap.put("salary",DESEncryptUtil.decryptBasedDes(currentSalary));
                    }else{
                        paramMap.put("salary","0");
                    }
                    paramMap.put("birDate",executeQuery.getString("bir_date"));
                    paramMap.put("employeeDate",executeQuery.getString("employee_date"));
                    paramMap.put("educate",executeQuery.getString("educate_degree"));
                    /*if(!StringUtils.equalsAny( executeQuery.getString("educate_degree"),"硕士研究生")){
                        continue;
                    }*/
                    paramMap.put("idCard",executeQuery.getString("id_no"));
                    paramMap.put("phone",executeQuery.getString("tel_number"));
                    paramMap.put("address",executeQuery.getString("bir_address"));
                    paramMap.put("section",executeQuery.getString("section"));
                    paramMap.put("lastSynTime",executeQuery.getString("last_syn_time"));
                    resultList.add(paramMap);
                    
                    paramArray[0]=executeQuery.getString("cust_name");
                    paramArray[1]=executeQuery.getString("current_job");
                    if(StringUtils.isNoneBlank(currentSalary)){
                        paramArray[2]=DESEncryptUtil.decryptBasedDes(currentSalary);
                    }else{
                        paramArray[2]="0";
                    }
                    paramArray[3]=executeQuery.getString("bir_date");
                    paramArray[4]=executeQuery.getString("employee_date");
                    paramArray[5]=executeQuery.getString("educate_degree");
                    paramArray[6]=executeQuery.getString("id_no");
                    paramArray[7]=executeQuery.getString("tel_number");
                    paramArray[8]=executeQuery.getString("bir_address");
                    paramArray[9]=executeQuery.getString("section");
                    paramArray[10]=executeQuery.getString("last_syn_time");
                    contentList.add(paramArray);
                }
                logger.warn("pageIndex为:"+pageIndex);
                sql =null;
                Thread.sleep(2);
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       Collections.sort(resultList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                String salary1 = o1.get("salary");
                String salary2 = o2.get("salary");
                if(StringUtils.isBlank(salary1)){
                    return -1;
                }
                if(StringUtils.isBlank(salary2)){
                    return 1;
                }
                try {
                    Double.valueOf(salary1);
                    Double.valueOf(salary2);
                } catch (NumberFormatException e) {
                    return -1;
                }
                return Double.valueOf(salary2).compareTo(Double.valueOf(salary1));
            }
        });
        logger.warn("封装数据结果为:{}",JSON.toJSONString(resultList));
        logger.warn("cast : " + (System.currentTimeMillis() - begin)/1000  + " S");// 241369 ms
        logger.warn("测试结束了.totalPage="+totalPage);
        
        //开始导出excel数据
        String[] titleStrs=new String[]{"name","job","salary","birDate","employeeDate","educate","idCard","phone","address","section","lastSynTime"};
        String filename="员工基本信息表";
        String sheetName="信息主表";
        try {
            logger.warn("解析excel信息开始...");
            
            ExcelWriter.writeExcel2007(titleStrs, contentList, filename,sheetName,null);
            logger.warn("解析excel信息结束...");
        } catch (IOException e) {
            logger.error("解析excel信息异常...",e);
        }
        
    }
    
    /**
     * 1000*2m==1000*10*1000
     */
    @Test
    public void testBatchInsertFromMysql() throws Exception {
        logger.warn("测试开始了.totalPage="+totalPage);
        // 开时时间
        Long begin =System.currentTimeMillis() ;
        java.sql.Connection conn = getConn();
        try {
            // 设置事务为非自动提交
            conn.setAutoCommit(true);
            // Statement st = conn.createStatement();
            // 比起st，pst会更好些
            PreparedStatement pst = conn.prepareStatement("");
            Map<String, Object> paramMap=new HashMap<String, Object>();
            // 外层循环，总提交事务次数
            for (int pageIndex= 0; pageIndex < 2/*10*//*totalPage*/; pageIndex++) {
                //int pageIndex=0;
                int startIndex=pageIndex*batchSize;
                int pageSize=batchSize;
                // sql前缀
                String sql = "SELECT * FROM yh_member LIMIT "+startIndex+", "+pageSize;
                //logger.warn("sql为:"+sql);
                // 添加执行sql
                ResultSet executeQuery = pst.executeQuery(sql);
                while(executeQuery.next()){
                    String userID = executeQuery.getString("userID");
                    paramMap.put(userID,userID);
                    //redisCacheService.addByKey(userID, userID);
                }
                logger.warn("pageIndex为:"+pageIndex);
                sql =null;
                Thread.sleep(2);
            }
            redisCacheService.batchAddByKey(paramMap);
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.warn("cast : " + (System.currentTimeMillis() - begin)/1000  + " S");// 241369 ms
        logger.warn("测试结束了.totalPage="+totalPage);
    }
    
    //@Test
    public void testBatchInsertFromJava() throws Exception {
        logger.warn("测试开始了...");
        // 开时时间
        Long begin =System.currentTimeMillis() ;
        String[] addressCodes=new String[]{"362421","659001","659000","654326","654325"};
        // sql前缀
        String prefix = "INSERT INTO tb_big_data (count, create_time, random) VALUES ";
        java.sql.Connection conn = getConn();
        try {
            // 保存sql后缀
            StringBuffer suffix = new StringBuffer();
            // 设置事务为非自动提交
            conn.setAutoCommit(false);
            // Statement st = conn.createStatement();
            // 比起st，pst会更好些
            PreparedStatement pst = conn.prepareStatement("");
            // 外层循环，总提交事务次数
            for (int i = 0; i < 1000; i++) {
                //List<String> resultList = IdcardUtils.generateIdCard(addressCodes[i]);
                List<String> resultList = IdcardUtils.generateIdCard(addressCodes[i%addressCodes.length]);
                int size = resultList.size();
                // 第次提交步长
                for (int j = 0; j <size ; j++) {
                    // 构建sql后缀
                    suffix.append("(" + size + ", SYSDATE(), '" + resultList.get(j) + "'),");
                    if(j%batchSize==0){
                        if(StringUtils.isNoneBlank(suffix)){
                            // 构建完整sql
                            String sql = prefix + suffix.substring(0, suffix.length() - 1);
                            logger.warn("sql为:"+sql);
                            // 添加执行sql
                            pst.addBatch(sql);
                            // 执行操作
                            pst.executeBatch();
                            // 提交事务
                            conn.commit();
                            // 清空上一次添加的数据
                            suffix = new StringBuffer();
                        }
                        Thread.sleep(2);
                    }
                }
                
                if(StringUtils.isNoneBlank(suffix)){
                    // 构建完整sql
                    String sql = prefix + suffix.substring(0, suffix.length() - 1);
                    logger.warn("sql为:"+sql);
                    // 添加执行sql
                    pst.addBatch(sql);
                    // 执行操作
                    pst.executeBatch();
                    // 提交事务
                    conn.commit();
                    // 清空上一次添加的数据
                    suffix = new StringBuffer();
                }
            }
            // 头等连接
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 耗时
        logger.warn("cast : " + (System.currentTimeMillis() - begin)  + " ms");
        logger.warn("测试结束了...");
    }

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://localhost:3306/test_db";
//        String username = "root";
//        String password = "root";
//        String url = "jdbc:mysql://10.0.68.215:3306/loan_front";
//        String username = "root";
//        String password = "yhjr2016YH";
        String url = "jdbc:mysql://localhost:3306/bi-baobiao";
        String username = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,加载对应驱动
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
