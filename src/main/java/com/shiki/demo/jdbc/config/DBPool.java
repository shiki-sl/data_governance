package com.shiki.demo.jdbc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @author shiki
 */
public class DBPool {
    /**
     * 使用LinkedList集合存放数据库连接
     */
    private static final LinkedList<Connection> CONN_POOL = new LinkedList<>();

    public static String db;

    public DBPool(DBConfig config) {
        init(config);
    }

    private static void init(DBConfig config) {
        InputStream in = DBPool.class.getResourceAsStream("/application.properties");
        String url;
        String user;
        String password;
        try {
            Properties prop = new Properties();
            prop.load(in);
            String driver = prop.getProperty("driver");
            if (config == null) {
                url = prop.getProperty("url");
                db = prop.getProperty("db");
                user = prop.getProperty("user");
                password = prop.getProperty("password");
            } else {
                url = config.url;
                db = config.dbName;
                user = config.username;
                password = config.password;
            }
            // 数据库连接池的初始化连接数的大小
            int initSize = Integer.parseInt(prop.getProperty("initSize"));
            // 加载驱动
            Class.forName(driver);
            for (int i = 0; i < initSize; i++) {
                Connection conn = DriverManager.getConnection(url, user, password);
                // 将创建的连接添加的list中
                System.out.println("初始化数据库连接池，创建第 " + (i + 1) + " 个连接，添加到池中");
                CONN_POOL.add(conn);
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection() {
        if (CONN_POOL.size() > 0) {
            //从集合中获取一个连接
            final Connection conn = CONN_POOL.removeFirst();
            //返回Connection的代理对象
            return (Connection) Proxy.newProxyInstance(DBPool.class.getClassLoader(), conn.getClass().getInterfaces(), (proxy, method, args) -> {
                if (!"close".equals(method.getName())) {
                    return method.invoke(conn, args);
                } else {
                    CONN_POOL.add(conn);
                    System.out.println("关闭当前连接，把连接还给连接池.........");
                    System.out.println("池中连接数为 " + CONN_POOL.size());
                    return null;
                }
            });
        } else {
            throw new RuntimeException("数据库繁忙，稍后再试............");
        }
    }

    @AllArgsConstructor
    @Builder
    @Data
    @Accessors(chain = true, fluent = true)
    static class DBConfig {
        private String dbName;
        private String url;
        private String username;
        private String password;
    }
}