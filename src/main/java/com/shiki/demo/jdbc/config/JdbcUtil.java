package com.shiki.demo.jdbc.config;

import java.sql.Connection;

/**
 * @author shiki
 */
public class JdbcUtil {

    /**
     * 数据库连接池
     */
    private static final DBPool CONN_POOL = new DBPool(null);

    /**
     * 从池中获取一个连接
     */
    public static Connection getConnection() {
        return CONN_POOL.getConnection();
    }
    /**
     * 从池中获取一个连接
     */
    public static Connection getClearConnection() {
        return CONN_POOL.getClearConnection();
    }
}