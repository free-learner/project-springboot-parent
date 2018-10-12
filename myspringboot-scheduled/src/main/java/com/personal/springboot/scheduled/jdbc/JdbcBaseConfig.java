package com.personal.springboot.scheduled.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.common.excel.ExcelWriter;

/**
 * 报表查询基礎配置类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年07月04日
 */
public class JdbcBaseConfig {
    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcBaseConfig.class);

    //public static final int batchSize = 10000/**10*/;
    public static final int batchSize = ExcelWriter.batchFlushSize;
    public static final int maxPageSize = 100;
    private static int totalCount;
    private static int totalPage;
    
    protected static Connection getConn(boolean productFlag) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/bi-baobiao?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        String username = "root";
        String password = "root";
        if(productFlag){
            LOGGER.error("#########数据库初始化环境为生产环境,请谨慎操作!##########");
            url = "jdbc:mysql://10.1.34.185:3306/loan_front?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
            //username = "loan_front";
            //password = "LSDLfllvzcvf";
            username = "80702457";
            password = "LSDlfllzcxvadff";
        }
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            LOGGER.error("数据库初始化成功...");
        } catch (ClassNotFoundException e) {
            LOGGER.error("数据库初始化Class.forName异常",e);
        } catch (SQLException e) {
            LOGGER.error("数据库初始化Class.forName异常",e);
        }
        LOGGER.error("数据库初始化结束...");
        return conn;
    }

    protected static void setTotalCount(int totalCount) {
        JdbcBaseConfig.totalCount = totalCount;
    }

    protected static int getTotalPage() {
        totalPage = totalCount % batchSize == 0 ? (totalCount / batchSize) : totalCount / batchSize + 1;
        LOGGER.info("當前totalPage的值为:{}", totalPage);
        return totalPage;
    }
    
    protected static int caculateTotalCount(String tableName,boolean productFlag) {
        String sql="select count(1) as totalCount from "+tableName;
        java.sql.Connection conn = getConn(productFlag);
        try {
            conn.setAutoCommit(true);
            PreparedStatement pst = conn.prepareStatement("");
            LOGGER.warn("caculateTotalCount最终查询sql为:{}",sql);
            ResultSet executeQuery = pst.executeQuery(sql);
            while(executeQuery.next()){
                totalCount = executeQuery.getInt(1);  
                //totalCount = executeQuery.getInt("totalCount"); 
            }
            LOGGER.warn("caculateTotalCount查询totalCount为:{}",totalCount);
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("caculateTotalCount查询数据库信息异常", e);
        }
        return totalCount;
    }

}
