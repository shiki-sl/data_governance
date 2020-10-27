package com.shiki.demo.jdbc.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author shiki
 */
public class JdbcUtil {

    /**
     * 数据库连接池
     */
    private static final DBPool CONN_POOL = new DBPool();

    /**
     * 从池中获取一个连接
     */
    public static Connection getConnection() {
        return CONN_POOL.getConnection();
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(Connection conn, Statement st, ResultSet rs) throws SQLException {

        // 关闭存储查询结果的ResultSet对象
        if (rs != null) {
            rs.close();
        }

        //关闭Statement对象
        if (st != null) {
            st.close();
        }

        //关闭连接
        if (conn != null) {
            conn.close();
        }
    }

}