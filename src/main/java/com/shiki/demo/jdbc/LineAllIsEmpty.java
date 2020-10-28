package com.shiki.demo.jdbc;

import com.google.common.collect.Sets;
import com.shiki.demo.jdbc.config.DBPool;
import com.shiki.demo.jdbc.config.JdbcUtil;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple2;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.shiki.demo.jdbc.constants.JdbcConstants.*;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.*;

/**
 * @Author shiki
 * @description: 全部列为空的数据
 * @Date 2020/10/26 下午2:54
 * <p>
 * 不需要进行操作和修改的常量位于{@link com.shiki.demo.jdbc.constants.JdbcConstants}中
 * <p>
 * {@link #main(String[]) 入口}
 * <p>
 * @see #findAny 使用单表进行测试,只限于生成sql脚本和空字段文件,对于新旧数据库字段变更不起效,默认关闭
 * <p>
 * @see #root_path 文件输出目录,需要保证该位置是一个文件夹,会在文件夹下生成四个独立文件,修改请见{@link #update_sql_path},
 * {@link #del_column_path},{@link #empty_column_path},{@link #modify_path} 独立文件详情见{@link #root_path} 上方字段注释
 * {@link #del_table}
 * <p>
 * @see #oldDB 旧库名 , {@link com.shiki.demo.jdbc.config.DBPool#db} 新库名
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

    static String oldDB = "ccxi_crc_proj";

    /**
     * 文件输出位置
     * update_sql_path 添加json,初始化json,赋值json
     * del_column_path删除源字段
     * empty_column_path 空字段列表集合
     * modify_path 新旧库的字段变化
     * del_table 不需要的表
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午5:40
     */
    final String root_path = "/home/shiki/code/output/";
    final String update_sql_path = root_path + "update_sql";
    final String del_column_path = root_path + "del_column";
    final String empty_column_path = root_path + "empty_column";
    final String modify_path = root_path + "modify";
    final String del_table = root_path + "del_table";

    /**
     * 取得全部表名
     *
     * @Author: shiki
     * @Date: 2020/10/28 上午10:18
     */
    final static Function2<String, Statement, Tuple2<List<String>, List<String>>> GET_ALL_TABLE_NAME = (db, state) -> {
        List<String> tableNames = new ArrayList<>(256);
        List<String> deprecatedTable = new ArrayList<>(256);
        try (ResultSet resultSet = state.executeQuery(String.format(ALL_TABLE_NAME, db))) {
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                final String tableComment = resultSet.getString("table_comment");
                if (!"view".equalsIgnoreCase(tableComment)) {
                    tableNames.add(tableName);
                }
                if (tableComment.startsWith("-无效表")) {
                    deprecatedTable.add(tableName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Tuple2<>(tableNames, deprecatedTable);
    };

    /**
     * 获取表全部列
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午1:42
     */
    final static Function3<String, String, Statement, List<String>> GET_ALL_COLUMN = (tableName, db, state) -> {
        System.out.println("tableSchema " + db + " tableName = " + tableName);
        List<String> columnNames = new ArrayList<>(64);
        final String format = String.format(ALL_COLUMN_NAME, db, tableName);
        try (ResultSet resultSet = state.executeQuery(format)) {
            while (resultSet.next()) {
                columnNames.add(resultSet.getString("column_name"));
            }
        } catch (SQLException e) {
            FAIL_TABLE_NAME.add(tableName);
            System.out.println(format);
            e.printStackTrace();
        }
        return columnNames;
    };

    /**
     * 获取数据库更新信息
     *
     * @return java.util.Map<java.lang.String, io.vavr.Tuple2 < java.lang.String, java.util.List < java.lang.String>>>
     * @Author: shiki
     * @Date: 2020/10/28 下午3:31
     */
    public Map<String, Tuple2<String, List<String>>> dbUpdate() {
        final HashMap<String, Tuple2<String, List<String>>> map = new HashMap<>(4);
        String addTable = "+ table";
        String dropTable = "- table";
        String addColumn = "添加 column";
        String dropColumn = "删除 column";
        final Tuple2<List<String>, List<String>> tuple2 = conn(GET_ALL_TABLE_NAME.apply(DBPool.db)).orElseThrow(RuntimeException::new);
        final List<String> newTable = tuple2._1;
        final List<String> oldTable = conn(GET_ALL_TABLE_NAME.apply(oldDB)).orElseThrow(RuntimeException::new)._1;
        final Sets.SetView<String> addTables = Sets.difference(new HashSet<>(newTable), new HashSet<>(oldTable));
        map.put(addTable, new Tuple2<>("添加表", new ArrayList<>(addTables)));

        final Sets.SetView<String> dropTables = Sets.difference(new HashSet<>(oldTable), new HashSet<>(newTable));
        map.put(dropTable, new Tuple2<>("删除表", new ArrayList<>(dropTables)));

        Sets.intersection(new HashSet<>(oldTable), new HashSet<>(newTable))
                .forEach(tableName -> {
                    final List<String> oldColumn = conn(GET_ALL_COLUMN.apply(tableName, DBPool.db)).orElseGet(ArrayList::new).stream().map(String::toLowerCase).collect(toList());
                    final List<String> newColumn = conn(GET_ALL_COLUMN.apply(tableName, oldDB)).orElseGet(ArrayList::new).stream().map(String::toLowerCase).collect(toList());
                    final List<String> add = newColumn.stream().filter(column -> !oldColumn.contains(column)).collect(toList());
                    if (add.size() > 0) {
                        map.put(tableName, new Tuple2<>(addColumn, add));
                    }
                    final List<String> drop = oldColumn.stream().filter(column -> !newColumn.contains(column)).collect(toList());
                    if (drop.size() > 0) {
                        map.put(tableName, new Tuple2<>(dropColumn, drop));
                    }
                });
        return map;
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
        try (ResultSet resultSet = statement.executeQuery(String.format(ALL_TABLE_NAME, DBPool.db));
             PrintStream delTable = new PrintStream(new File(del_table))
        ) {
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                final String tableComment = resultSet.getString("table_comment");
                if (!tableComment.startsWith("-无效表") && !INVALID_TABLES.contains(tableName)) {
                    tableNames.add(tableName);
                } else if (tableComment.startsWith("-无效表")) {
                    delTable.println(DROP_TABLE + tableName);
                }
            }
        } catch (SQLException | FileNotFoundException e) {
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
        System.out.println("method tableName = " + tableName);
        List<String> columnNames = new ArrayList<>(64);
        Map<String, List<String>> map = new HashMap<>(255);
        final String format = String.format(ALL_COLUMN_NAME, DBPool.db, tableName);
        try (ResultSet resultSet = statement.executeQuery(format)) {
            while (resultSet.next()) {
                columnNames.add(resultSet.getString("column_name"));
            }
        } catch (SQLException e) {
            FAIL_TABLE_NAME.add(tableName);
            System.out.println(format);
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
        columnNames
//                .stream().filter(name -> !excludeColumn.contains(name))
                .forEach(
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
    void outTableFilterSql() {
        final Map<String, List<String>> maps = lineAllIsEmpty();
//        尝试设置输出位置,生成文件
        try (final PrintStream updateSql = new PrintStream(new File(update_sql_path));
             final PrintStream dropSql = new PrintStream(new File(del_column_path));
             final PrintStream emptyColumn = new PrintStream(new File(empty_column_path))) {
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
                final List<String> list = leftIntersection(v);
                if (list.size() > 0) {
                    emptyColumn.println(k + "    " + list);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出新旧库字段变化文件
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午3:39
     */
    void outBDUpdate() {
        try (final PrintStream modify = new PrintStream(new File(modify_path))) {
            dbUpdate().forEach((k, v) -> modify.println(k + "  " + v));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注入Statement,获取执行结果
     *
     * @param fun:被注入Statement的方法体
     * @return java.util.Optional<T> 执行结果
     * @Author: shiki
     * @Date: 2020/10/28 下午4:02
     */
    <T> Optional<T> conn(Function<Statement, T> fun) {
        try (Connection connection = JdbcUtil.getConnection();
             Statement statement = connection.createStatement()) {
            return Optional.ofNullable(fun.apply(statement));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
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

    public static void main(String[] args) {
        final long start = currentTimeMillis();
        final LineAllIsEmpty empty = new LineAllIsEmpty();
        EXECUTOR.submit(empty::outBDUpdate);
        EXECUTOR.submit(empty::outTableFilterSql);
        EXECUTOR.shutdown();
        System.out.println("-- 全部执行完毕,消耗总时长" + (currentTimeMillis() - start) + "毫秒");
    }
}