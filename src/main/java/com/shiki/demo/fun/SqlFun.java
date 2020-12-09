package com.shiki.demo.fun;

import com.shiki.demo.jdbc.config.JdbcUtil;

import java.sql.*;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author shiki
 * @description: 获取数据库连接资源的工具类
 * @Date 2020/11/23 下午2:22
 */
public interface SqlFun {

    /**
     * 注入查询sql,获取执行结果
     *
     * @param querySql:   要执行的sql
     * @param successFun: 成功执行成功后需要执行的方法体
     * @param catchFun:   异常时执行的方法体
     * @return java.util.Optional<T> 执行结果
     * @Author: shiki
     * @Date: 2020/10/28 下午4:02
     */
    static <T> Optional<T> query(String querySql, Function<ResultSet, T> successFun, Supplier<T> catchFun) {
        return Private.getStat(querySql, stat -> {
            try (final ResultSet resultSet = stat.executeQuery()) {
                return Optional.ofNullable(successFun.apply(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.ofNullable(catchFun.get());
            }
        });
    }

    static <T> Optional<T> query(String querySql, Function<ResultSet, T> successFun, Function<SQLException, T> catchFun) {
        return Private.getStat(querySql, stat -> {
            try (final ResultSet resultSet = stat.executeQuery()) {
                return Optional.ofNullable(successFun.apply(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.ofNullable(catchFun.apply(e));
            }
        });
    }

    static <T> Optional<T> query(String querySql, Function<ResultSet, T> successFun, Consumer<SQLException> catchFun) {
        return Private.getStat(querySql, stat -> {
            try (final ResultSet resultSet = stat.executeQuery()) {
                return Optional.ofNullable(successFun.apply(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
                catchFun.accept(e);
            }
            return Optional.empty();
        });
    }

    static <T> Optional<T> query(String querySql, Function<ResultSet, T> successFun) {
        return Private.getStat(querySql, stat -> {
            try (final ResultSet resultSet = stat.executeQuery()) {
                return Optional.ofNullable(successFun.apply(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 注入插入sql,获取执行结果
     *
     * @param querySql:   要执行的sql
     * @param successFun: 成功执行成功后需要执行的方法体
     * @return java.util.Optional<T> 执行结果
     * @Author: shiki
     * @Date: 2020/10/28 下午4:02
     */
    static <T> Optional<T> insert(String querySql, Function<PreparedStatement, T> successFun) {
        try (Connection connection = JdbcUtil.getClearConnection();
             final PreparedStatement statement = connection.prepareStatement(querySql);
        ) {
            final Optional<T> apply = Optional.ofNullable(successFun.apply(statement));
            connection.commit();
            return apply;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    class Private {
        /**
         * 私有化接口方法,用于获取资源
         *
         * @param sqlString:
         * @return java.sql.PreparedStatement
         * @Author: shiki
         * @Date: 2020/11/23 下午2:34
         */
        private static <T> T getStat(String sqlString, Function<PreparedStatement, T> function) {
            try (Connection connection = JdbcUtil.getConnection();
                 final PreparedStatement statement = connection.prepareStatement(sqlString);
            ) {
                return function.apply(statement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
