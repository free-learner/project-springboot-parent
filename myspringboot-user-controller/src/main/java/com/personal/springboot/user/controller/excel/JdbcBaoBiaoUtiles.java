package com.personal.springboot.user.controller.excel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 报表查询工具类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年6月19日
 */
public class JdbcBaoBiaoUtiles {

    public final static Logger LOGGER = LoggerFactory.getLogger(JdbcBaoBiaoUtiles.class);

    private static final int batchSize = 1000 * 10;
    private static int totalCount = 11628901;
    private static int totalPage;

    /**
     * 获取通讯录列表信息方法
     */
    public static void getUserContaxts() throws Exception {
        LOGGER.warn("测试开始了.totalPage=" + totalPage);
        Long begin = System.currentTimeMillis();
        java.sql.Connection conn = getConn();
        try {
            conn.setAutoCommit(true);
            PreparedStatement pst = conn.prepareStatement("");
            Map<String, Object> paramMap = new HashMap<String, Object>();
            for (int pageIndex = 0; pageIndex < 2; pageIndex++) {
                int startIndex = pageIndex * batchSize;
                int pageSize = batchSize;
                // sql前缀
                String sql = "SELECT * FROM yh_member LIMIT " + startIndex + ", " + pageSize;
                // logger.warn("sql为:"+sql);
                ResultSet executeQuery = pst.executeQuery(sql);
                while (executeQuery.next()) {
                    String userID = executeQuery.getString("userID");
                    paramMap.put(userID, userID);
                }
                LOGGER.warn("pageIndex为:" + pageIndex);
                sql = null;
                Thread.sleep(2);
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("查询数据库信息异常", e);
        }
        LOGGER.warn("cast : " + (System.currentTimeMillis() - begin) / 1000 + " S");
        LOGGER.warn("测试结束了.totalPage=" + totalPage);
    }

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/bi-baobiao";
        String username = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    public static int getTotalCount() {
        return totalCount;
    }

    public static void setTotalCount(int totalCount) {
        JdbcBaoBiaoUtiles.totalCount = totalCount;
    }

    public int getTotalPage() {
        totalPage = totalCount % batchSize == 0 ? (totalCount / batchSize) : totalCount / batchSize + 1;
        LOGGER.info("當前totalPage的值为:{}", totalPage);
        return totalPage;
    }

}
