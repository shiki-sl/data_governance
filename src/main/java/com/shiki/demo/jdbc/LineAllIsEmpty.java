package com.shiki.demo.jdbc;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.shiki.demo.jdbc.config.JdbcUtil;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;

import static com.shiki.demo.jdbc.constants.JdbcConstants.*;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @Author shiki
 * @description: 全部列为空的数据, todo 表rm_log已被排除, 因为过大导致查询超时
 * @Date 2020/10/26 下午2:54
 */
public class LineAllIsEmpty {

    /**
     * 是否使用单表进行测试
     * ,默认数据库的第一张表
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午5:44
     */
    boolean findAny = false;

    /**
     * todo 排除字段, 旧系统对表进行更新字段
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午5:49
     */
    List<String> excludeColumn = Collections.singletonList("OTHER_INDUSTRY_REASON");

    /**
     * 文件输出位置
     * update_sql_path 添加json,初始化json,赋值json
     * del_column_path删除源字段
     * empty_column_path 空字段列表集合
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午5:40
     */
    final String update_sql_path = "/home/shiki/code/output/update_sql";
    final String del_column_path = "/home/shiki/code/output/del_column";
    final String empty_column_path = "/home/shiki/code/output/empty_column";

    public static void main(String[] args) throws FileNotFoundException {
        final long start = currentTimeMillis();
        new LineAllIsEmpty().tableFilter();
        System.out.println("-- 全部执行完毕,消耗总时长" + (currentTimeMillis() - start) + "毫秒");
    }

    /**
     * 整个流程的入口和出口
     *
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>> 表名为key,列名为value
     * @Author: shiki
     * @Date: 2020/10/27 下午5:00
     */
    Map<String, List<String>> lineAllIsEmpty() {
        try (Connection connection = JdbcUtil.getConnection();
             Statement statement = connection.createStatement()) {
            final List<String> tableNames = getAllTableName(statement);
            System.out.println("tableName.size() = " + tableNames.size());
            System.out.println(tableNames);
            final Stream<Map<String, List<String>>> stream = tableNames.stream()
                    .map(tableName -> getTableAllLine(statement, tableName))
                    .filter(map -> map.size() > 0);
            final Map<String, List<String>> maps;
            if (findAny) {
//                单表测试
                maps = stream.findAny().orElseGet(HashMap::new);
            } else {
//                正常全表操作
                maps = stream
                        .flatMap(map -> map.entrySet().stream())
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            System.out.println("maps.size() = " + maps.size());
            System.out.println("maps = " + maps);
            System.out.println("JSON.toJSONString(maps) = " + JSON.toJSONString(maps));
            System.out.println("执行失败的表 failTableName = " + FAIL_TABLE_NAME);
            return maps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    /**
     * 获取数据库的全部表名
     *
     * @param statement:
     * @return java.util.List<java.lang.String> 表名集合
     * @Author: shiki
     * @Date: 2020/10/27 下午4:56
     */
    List<String> getAllTableName(Statement statement) {
        List<String> tableNames = new ArrayList<>(256);
        try (ResultSet resultSet = statement.executeQuery(ALL_TABLE_NAME)) {
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                final String tableComment = resultSet.getString("table_comment");
                if (!tableComment.startsWith("-无效表") && !INVALID_TABLES.contains(tableName)) {
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    /**
     * 根据表明获取全部列
     *
     * @param statement: 见jdbc文档
     * @param tableName: 根据表名获取全部列
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>> 列名集合
     * @Author: shiki
     * @Date: 2020/10/27 下午4:57
     */
    Map<String, List<String>> getTableAllLine(Statement statement, String tableName) {
        final long startTime = currentTimeMillis();
        System.out.println("tableName = " + tableName);
        List<String> columnNames = new ArrayList<>(64);
        Map<String, List<String>> map = new HashMap<>(255);
        try (ResultSet resultSet = statement.executeQuery(String.format(ALL_COLUMN_NAME, tableName))) {
            while (resultSet.next()) {
                columnNames.add(resultSet.getString("column_name"));
            }
        } catch (SQLException e) {
            FAIL_TABLE_NAME.add(tableName);
            e.printStackTrace();
        }
        if (columnNames.size() != 0) {
            final List<String> isAllEmpty = columnIsAllEmpty(statement, columnNames, tableName);
            if (isAllEmpty != null && isAllEmpty.size() != 0) {
                map.put(tableName, isAllEmpty);
            }
        }
        System.out.println("columnNames.size() = " + columnNames.size());
        HANDLE_TABLE.addAndGet(1);
        System.out.println("handleTable = " + HANDLE_TABLE);
        System.out.println("表: " + tableName + " 执行花费时间 = " + (currentTimeMillis() - startTime) + "毫秒");
        return map;
    }

    /**
     * 获取全部行为空的列名集合
     *
     * @param statement:   见jdbc文档
     * @param columnNames: 表下面的全部列
     * @param tableName:   sql中所需的条件,在该方法中不重要
     * @return java.util.List<java.lang.String>
     * @Author: shiki
     * @Date: 2020/10/27 下午4:57
     */
    List<String> columnIsAllEmpty(Statement statement, List<String> columnNames, String tableName) {
        if (ObjectUtils.isEmpty(columnNames)) {
            return Collections.emptyList();
        }
        List<String> allLineIsEmptyColumnNames = new ArrayList<>(64);
        StringBuilder sb = new StringBuilder();
        columnNames.stream().filter(name -> !excludeColumn.contains(name)).forEach(
                columnName -> sb
                        .append(UNION)
//                        替换条件
                        .append(TABLE_ALL_LINE_IS_NOT_EMPTY
                                .replaceAll(DYNAMIC_TABLE_NAME, tableName)
                                .replaceAll(DYNAMIC_COLUMN_NAME, columnName)));

        try (ResultSet resultSet = statement.executeQuery(PRE + sb.toString())) {
            while (resultSet.next()) {
                if (resultSet.getInt("count") == 0) {
                    allLineIsEmptyColumnNames.add(resultSet.getString("column_name"));
                }
            }
//            与统一字段进行合并
            final Sets.SetView<String> view = Sets.union(new HashSet<>(allLineIsEmptyColumnNames), new HashSet<>(INVALID_COLUMN));
            return new ArrayList<>(view);
        } catch (SQLException e) {
            System.out.println(PRE + sb.toString());
            FAIL_TABLE_NAME.add(tableName);
            e.printStackTrace();
        }
        return allLineIsEmptyColumnNames;
    }

    /**
     * 输出可执行sql和空字段集合到本地文件
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午4:58
     */
    void tableFilter() throws FileNotFoundException {
        final Map<String, List<String>> maps = lineAllIsEmpty();
//        尝试设置输出位置,生成文件
        final PrintStream updateSql = new PrintStream(new File(update_sql_path));
        final PrintStream dropSql = new PrintStream(new File(del_column_path));
        maps.forEach((k, v) -> {
            final String infoDetail = "history";
            updateSql.printf((ADD_COLUMN) + "%n", k, infoDetail);
            updateSql.printf((EMPTY_COLUMNS_2_JSON) + "%n", k, infoDetail);
//            json字段生成
            final String addJson = v.stream()
                    .map(str -> String.format(JSON_SET, infoDetail, infoDetail, str, str))
                    .collect(joining(","))
                    .replaceAll("\\[", "(").replaceAll("]", ")");
            updateSql.println(String.format(UPDATE_SET, k) + addJson + ";");
//            原字段删除
            final String drop = v.stream()
                    .map(str -> String.format(DROP, str))
                    .collect(joining(","))
                    .replaceAll("\\[", "").replaceAll("]", "");
            dropSql.println(String.format(ALTER_TABLE, k) + drop + ";");
        });
        final PrintStream emptyColumn = new PrintStream(new File(empty_column_path));
        Stream.of(maps)
                .peek(map -> map.forEach((k, v) -> maps.put(k, leftIntersection(v))))
                .forEach(emptyColumn::println);
    }

    /**
     * 集合间操作,自行查询guava文档
     *
     * @param list1 :
     * @return java.util.List<T>
     * @Author: shiki
     * @Date: 2020/10/27 下午4:59
     */
    private <T> List<T> leftIntersection(List<T> list1) {
        final Sets.SetView<T> view = Sets.difference(new HashSet<>(list1), new HashSet<>(INVALID_COLUMN));
        return new ArrayList<>(view);
    }

}