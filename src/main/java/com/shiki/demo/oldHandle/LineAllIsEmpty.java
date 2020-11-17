package com.shiki.demo.oldHandle;

import com.google.common.collect.Sets;
import com.shiki.demo.constants.BaseConstants;
import com.shiki.demo.jdbc.config.DBPool;
import com.shiki.demo.jdbc.config.JdbcUtil;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple2;
import lombok.val;
import lombok.var;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.shiki.demo.constants.BaseConstants.*;
import static com.shiki.demo.constants.OldHandleConstants.*;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.*;

/**
 * @Author shiki
 * @description: 全部列为空的数据
 * @Date 2020/10/26 下午2:54
 * <p>
 * 不需要进行操作和修改的常量位于{@link BaseConstants}中
 * <p>
 * {@link #main(String[]) 入口}
 * <p>
 * @see #FIND_ANY 使用单表进行测试,只限于生成sql脚本和空字段文件,对于新旧数据库字段变更不起效,默认关闭
 * <p>
 * @see BaseConstants#ROOT_PATH 文件输出目录,需要保证该位置是一个文件夹,会在文件夹下生成独立文件,修改请见{@link #UPDATE_SQL_PATH},
 * {@link #DEL_COLUMN_PATH},{@link #EMPTY_COLUMN_PATH},{@link #MODIFY_PATH} 独立文件详情见{@link BaseConstants#ROOT_PATH} 上方字段注释
 * {@link #DEL_TABLE}
 * <p>
 * @see #OLD_DB 旧库名 , {@link com.shiki.demo.jdbc.config.DBPool#db} 新库名
 */
public class LineAllIsEmpty {

    /**
     * 是否使用单表进行测试
     * ,默认数据库的第一张表
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午5:44
     */
    final static boolean FIND_ANY = false;

    /**
     * 就表名
     *
     * @Author: shiki
     * @Date: 2020/11/5 下午3:40
     */
    static final String OLD_DB = "ccxi_crc_proj";

    /**
     * 需要排除的字段
     *
     * @Author: shiki
     * @Date: 2020/11/5 下午3:39
     */
    static final List<String> OTHER = Arrays.asList("create_user_id", "create_user_name", "create_time", "usable_status");

    /**
     * @Author: shiki
     * @Date: 2020/11/16 下午2:35
     * @see #UPDATE_SQL_PATH 添加json,初始化json,赋值json
     * @see #DEL_COLUMN_PATH 删除源字段
     * @see #EMPTY_COLUMN_PATH 空字段列表集合
     * @see #MODIFY_PATH 新旧库的字段变化
     * @see #DEL_TABLE 不需要的表
     * @see #modify_TABLE 修改的表
     * @see #TABLE_COUNT_COLUMN 所有表的行数
     * @see #MODIFY_EMPTY_COLUMN_COMMENT 表中的空字段
     */
    static final String UPDATE_SQL_PATH = ROOT_PATH + "update_sql.sql";
    static final String DEL_COLUMN_PATH = ROOT_PATH + "del_column.sql";
    static final String EMPTY_COLUMN_PATH = ROOT_PATH + "empty_column";
    static final String MODIFY_PATH = ROOT_PATH + "modify";
    static final String DEL_TABLE = ROOT_PATH + "del_table.sql";
    static final String modify_TABLE = ROOT_PATH + "modify_table";
    static final String TABLE_COUNT_COLUMN = ROOT_PATH + "count_column.txt";
    static final String MODIFY_EMPTY_COLUMN_COMMENT = ROOT_PATH + "modify_empty_column_comment";

    static final List<String> last_table = new ArrayList<>();

    /*
     * 取出上次删除的表
     * @Author: shiki
     * @Date: 2020/11/13 下午3:42
     */
    static {
        final Pattern compile = Pattern.compile(DROP_TABLE);
        final File file = new File(DEL_TABLE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String str;
                while ((str = reader.readLine()) != null) {
                    if (str.startsWith("drop")) {
                        last_table.add(compile.matcher(str).replaceAll(""));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        last_table.forEach(System.out::println);
    }

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
     * 取得全部外键
     *
     * @Author: shiki
     * @Date: 2020/10/29 下午3:00
     */
    final static Function<Statement, List<String>> GET_DROP_PK = state -> {
        try (val resultSet = state.executeQuery(DROP_PK)) {
            final List<String> pk = new ArrayList<>();
            while (resultSet.next()) {
                pk.add(resultSet.getString(DEL_PK));
            }
            return pk;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    };

    /**
     * 获取数据库有效表名
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午4:56
     */
    final static Function<Statement, List<String>> GET_VALID_TABLE_NAME = state -> {
        List<String> tableNames = new ArrayList<>(256);
        try (ResultSet resultSet = state.executeQuery(String.format(ALL_TABLE_NAME, DBPool.db));
             PrintStream delTable = new PrintStream(new File(DEL_TABLE));
             PrintStream excludeTable = new PrintStream(new File(modify_TABLE))
        ) {
//            删除表之前先清空外键
            conn(GET_DROP_PK).orElseGet(ArrayList::new).forEach(delTable::println);
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                final String tableComment = resultSet.getString("table_comment");
                if (!tableComment.startsWith("-无效表") && !INVALID_TABLES.contains(tableName)) {
                    tableNames.add(tableName);
                } else if (tableComment.startsWith("-无效表")) {
                    delTable.println(DROP_TABLE + tableName + ";");
                    excludeTable.println(tableName);
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return tableNames;
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
     * 计算全部表的列总数
     *
     * @Author: shiki
     * @Date: 2020/10/29 下午3:29
     */
    final static Function2<String, Statement, Integer> GET_TABLE_COUNT_COLUMN = (tableName, state) -> {
        try (var resultSet = state.executeQuery(String.format(TABLE_COLUMN_COLUMN, DBPool.db, tableName))) {
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    };

    /**
     * 根据表名和列名获取表中除了操作信息(创建修改删除等列)之外的全部空列
     *
     * @Author: shiki
     * @Date: 2020/11/2 下午3:13
     */
    final static Function3<String, List<String>, Statement, List<String>> EMPTY_COLUMN_COMMENT = (tableName, columnNames, state) -> {
        List<String> allLineIsEmptyColumnNames = new ArrayList<>(64);
        StringBuilder sb = new StringBuilder();
        columnNames.forEach(
                columnName -> sb
                        .append(UNION)
//                        替换条件
                        .append(TABLE_ALL_LINE_IS_NOT_EMPTY
                                .replaceAll(DYNAMIC_TABLE_NAME, tableName)
                                .replaceAll(DYNAMIC_COLUMN_NAME, columnName)));

        try (ResultSet resultSet = state.executeQuery(PRE + sb.toString())) {
            while (resultSet.next()) {
                final String columnName = resultSet.getString("column_name");
                if (resultSet.getInt("count") == 0 && !INVALID_COLUMN.contains(columnName)) {
                    allLineIsEmptyColumnNames.add(columnName);
                }
            }
            return allLineIsEmptyColumnNames;
        } catch (SQLException e) {
            System.out.println(PRE + sb.toString());
            e.printStackTrace();
        }
        return allLineIsEmptyColumnNames;
    };

    /**
     * 获取数据库更新信息
     * <p>
     * tuple2在此返回值中作为简单键值对表示形式 ._1表示key, ._2表示值
     * 例如   addColumn user_id; 表示数据库中新增一行user_id的列
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午3:31
     */
    final static Supplier<Map<String, Tuple2<String, List<String>>>> DB_UPDATE = () -> {
        final HashMap<String, Tuple2<String, List<String>>> map = new HashMap<>(4);
        String addTable = "+ table";
        String dropTable = "- table";
        String addColumn = "添加 column";
        String dropColumn = "删除 column";
        val tuple2 = conn(GET_ALL_TABLE_NAME.apply(DBPool.db)).orElseThrow(RuntimeException::new);
        final List<String> newTable = tuple2._1;
        final List<String> oldTable = conn(GET_ALL_TABLE_NAME.apply(OLD_DB)).orElseThrow(RuntimeException::new)._1;
        final var addTables = Sets.difference(new HashSet<>(newTable), new HashSet<>(oldTable));
        map.put(addTable, new Tuple2<>("添加表", new ArrayList<>(addTables)));

        final Sets.SetView<String> dropTables = Sets.difference(new HashSet<>(oldTable), new HashSet<>(newTable));
        map.put(dropTable, new Tuple2<>("删除表", new ArrayList<>(dropTables)));

        Sets.intersection(new HashSet<>(oldTable), new HashSet<>(newTable))
                .forEach(tableName -> {
                    final List<String> oldColumn = conn(GET_ALL_COLUMN.apply(tableName, DBPool.db)).orElseGet(ArrayList::new).stream().map(String::toLowerCase).collect(toList());
                    final List<String> newColumn = conn(GET_ALL_COLUMN.apply(tableName, OLD_DB)).orElseGet(ArrayList::new).stream().map(String::toLowerCase).collect(toList());
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
    };

    /**
     * 输出全部的空字段注释变更sql并保存到本地
     *
     * @Author: shiki
     * @Date: 2020/11/2 下午3:57
     */
    final static Consumer<String> PRINTF_MODIFY_EMPTY_COLUMN_COMMENT = str -> {
        try (PrintStream ps = new PrintStream(str)) {
            final List<String> tableNames = conn(GET_VALID_TABLE_NAME).orElseGet(Collections::emptyList);
            tableNames.forEach(tableName -> {
                        List<String> columnNames = conn(GET_ALL_COLUMN.apply(tableName, DBPool.db)).orElseGet(Collections::emptyList);
                        conn(EMPTY_COLUMN_COMMENT.apply(tableName, columnNames)).filter(list -> list.size() > 0).ifPresent(ps::println);
                    }
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };

    /**
     * 整个流程的入口和出口
     *
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>> 表名为key,列名为value
     * @Author: shiki
     * @Date: 2020/10/27 下午5:00
     */
    static Map<String, List<String>> lineAllIsEmpty() {
        try (Connection connection = JdbcUtil.getConnection();
             Statement statement = connection.createStatement()) {
            final List<String> tableNames = conn(GET_VALID_TABLE_NAME).orElseGet(Collections::emptyList);
            System.out.println("tableName.size() = " + tableNames.size());
            System.out.println(tableNames);
            final Stream<Map<String, List<String>>> stream = tableNames.stream()
                    .map(tableName -> getTableAllLine(statement, tableName))
                    .filter(map -> map.size() > 0);
            final Map<String, List<String>> maps;
            if (FIND_ANY) {
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
     * 根据表名获取全部列
     *
     * @param statement: 见jdbc文档
     * @param tableName: 根据表名获取全部列
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>> 列名集合
     * @Author: shiki
     * @Date: 2020/10/27 下午4:57
     */
    static Map<String, List<String>> getTableAllLine(Statement statement, String tableName) {
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
    static List<String> columnIsAllEmpty(Statement statement, List<String> columnNames, String tableName) {
        if (ObjectUtils.isEmpty(columnNames)) {
            return Collections.emptyList();
        }
        List<String> allLineIsEmptyColumnNames = new ArrayList<>(64);
        StringBuilder sb = new StringBuilder();
        columnNames
                .forEach(
                        columnName -> sb
                                .append(UNION)
//                        替换条件
                                .append(TABLE_ALL_LINE_IS_NOT_EMPTY
                                        .replaceAll(DYNAMIC_TABLE_NAME, tableName)
                                        .replaceAll(DYNAMIC_COLUMN_NAME, columnName)));

        try (ResultSet resultSet = statement.executeQuery(PRE + sb.toString())) {
            while (resultSet.next()) {
                final String columnName = resultSet.getString("column_name");
                if (resultSet.getInt("count") == 0 || INVALID_COLUMN.contains(columnName)) {
                    allLineIsEmptyColumnNames.add(columnName);
                }
            }
            return allLineIsEmptyColumnNames;
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
    static void outTableFilterSql() {
        final Map<String, List<String>> maps = lineAllIsEmpty();
//        尝试设置输出位置,生成文件
        try (final PrintStream updateSql = new PrintStream(new File(UPDATE_SQL_PATH));
             final PrintStream dropSql = new PrintStream(new File(DEL_COLUMN_PATH));
             final PrintStream emptyColumn = new PrintStream(new File(EMPTY_COLUMN_PATH))) {
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
//                        删除的sql中需要保留的字段
                        .filter(str -> !OTHER.contains(str))
//                        删除的sql需要保留的字段
//                        .filter(str -> !MAP.containsKey(k) || !MAP.get(k).contains(str))
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
     * 输出全部表的字段总数
     *
     * @Author: shiki
     * @Date: 2020/10/29 下午3:42
     */
    static void getAllTableColumn() {
        try (var out = new PrintStream(TABLE_COUNT_COLUMN)) {
            conn(GET_ALL_TABLE_NAME.apply(DBPool.db)).ifPresent(var -> var._1.forEach(str -> {
                out.print(str + ":  ");
                conn(GET_TABLE_COUNT_COLUMN.apply(str)).ifPresent(out::println);
            }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出空字段
     *
     * @Author: shiki
     * @Date: 2020/11/2 下午3:49
     */
    static void outEmptyColumn() {
        PRINTF_MODIFY_EMPTY_COLUMN_COMMENT.accept(MODIFY_EMPTY_COLUMN_COMMENT);
    }

    /**
     * 输出新旧库字段变化文件
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午3:39
     */
    static void outUpdate() {
        try (final PrintStream modify = new PrintStream(new File(MODIFY_PATH))) {
            DB_UPDATE.get().forEach((k, v) -> modify.println(k + "  " + v));
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
    static <T> Optional<T> conn(Function<Statement, T> fun) {
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
    static <T> List<T> leftIntersection(List<T> list1) {
        final Sets.SetView<T> view = Sets.difference(new HashSet<>(list1), new HashSet<>(INVALID_COLUMN));
        return new ArrayList<>(view);
    }

    public static void main(String[] args) {
        final long start = currentTimeMillis();
        EXECUTOR.submit(LineAllIsEmpty::outUpdate);
        EXECUTOR.submit(LineAllIsEmpty::outTableFilterSql);
        EXECUTOR.submit(LineAllIsEmpty::getAllTableColumn);
        EXECUTOR.submit(LineAllIsEmpty::outEmptyColumn);

        EXECUTOR.shutdown();
        System.out.println("-- 全部执行完毕,消耗总时长" + (currentTimeMillis() - start) + "毫秒");
    }

}