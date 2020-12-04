package com.shiki.demo.oldHandle;

import com.google.common.collect.Sets;
import com.shiki.demo.constants.BaseConstants;
import com.shiki.demo.constants.MergeRule;
import com.shiki.demo.fun.SqlFun;
import com.shiki.demo.funOld2New.GenerateFun;
import com.shiki.demo.jdbc.config.DBPool;
import com.shiki.demo.jdbc.config.JdbcUtil;
import com.shiki.demo.movedb.SingletonMapRule;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Tuple2;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.ObjectUtils;

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

import static com.shiki.demo.funOld2New.GenerateFun.*;
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
 * @see BaseConstants#ROOT_PATH 文件输出目录,需要保证该位置是一个文件夹,会在文件夹下生成独立文件,修改请见{@link #_1_COPY_INVALID_COLUMN_TO_JSON},
 * {@link #_2_DEL_INVALID_COLUMN} 独立文件详情见{@link BaseConstants#ROOT_PATH} 上方字段注释
 * {@link #_1_DEL_INVALID_TABLE}
 * <p>
 *  {@link com.shiki.demo.jdbc.config.DBPool#db} 新库名
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

    final static Pattern DELIMITER = Pattern.compile("`");
    final static Pattern SEMICOLON = Pattern.compile(";");

    /**
     * 需要排除的字段
     *
     * @Author: shiki
     * @Date: 2020/11/5 下午3:39
     */
    static final List<String> OTHER = Arrays.asList("create_time", "usable_status");

    /**
     * @Author: shiki
     * @Date: 2020/11/16 下午2:35
     * @see #_1_COPY_INVALID_COLUMN_TO_JSON 添加json,初始化json,赋值json
     * @see #_2_DEL_INVALID_COLUMN 删除源字段
     * @see #_1_DEL_INVALID_TABLE 不需要的表
     */

    static final String _1_COPY_INVALID_COLUMN_TO_JSON = OLD_CLEAR + "1_copy_invalid_column_to_json.sql";
    static final String _1_SET_JSON = OLD_CLEAR_COLUMN_UPDATE + "1_set_json.sql";
    static final String _1_DEL_INVALID_TABLE = CLEAR_DEV + "1_del_invalid_table.sql";
    static final String _2_DEL_INVALID_COLUMN = CLEAR_DEV + "2_del_invalid_column.sql";

    public static final List<String> last_table = new ArrayList<>();

    /*
     * 取出上次删除的表
     * @Author: shiki
     * @Date: 2020/11/13 下午3:42
     */
    static {
        final Pattern compile = Pattern.compile(DROP_TABLE);
        final File file = new File(_1_DEL_INVALID_TABLE);
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
     * 获取数据库有效表名
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午4:56
     */
    final static Function<Statement, List<String>> GET_VALID_TABLE_NAME = state -> {
        List<String> tableNames = new ArrayList<>(256);
        try (ResultSet resultSet = state.executeQuery(String.format(ALL_TABLE_NAME, DBPool.db, DBPool.db));
             PrintStream delTable = new PrintStream(new File(_1_DEL_INVALID_TABLE));
        ) {
//            删除表之前先清空外键
//            conn(GET_DROP_PK).orElseGet(ArrayList::new).forEach(delTable::println);
            delTable.println("/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;");
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                final String tableComment = resultSet.getString("table_comment");
                if (!tableComment.startsWith("-无效表") && !INVALID_TABLES.contains(tableName)) {
                    tableNames.add(tableName);
                } else if (tableComment.startsWith("-无效表")) {
                    delTable.println(DROP_TABLE + tableName + ";");
                }
            }
            delTable.println("/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return tableNames;
    };

    final static Function2<String, Statement, List<String>> findColumnTypeIsDate = (tableName, stat) -> {
        final ArrayList<String> list = new ArrayList<>();
        try (final ResultSet resultSet = stat.executeQuery(String.format(EXCLUDE_DATE_TYPE, tableName, DBPool.db))) {
            while (resultSet.next()) {
                list.add(resultSet.getString("column_name"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
        final List<String> list = conn(findColumnTypeIsDate.apply(tableName)).orElseGet(ArrayList::new);
        StringBuilder sb = new StringBuilder();
        columnNames.forEach(columnName -> sb
                .append(UNION).append(
                        String.format(TABLE_ALL_LINE_IS_NOT_NULL, list.contains(columnName) ? " " : TABLE_ALL_LINE_IS_NOT_EMPTY)
//                                替换条件
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
        try (final PrintStream updateSql = new PrintStream(new File(_1_COPY_INVALID_COLUMN_TO_JSON));
             final PrintStream dropSql = new PrintStream(new File(_2_DEL_INVALID_COLUMN));
             final PrintStream setJson = new PrintStream(new File(_1_SET_JSON))) {
            maps.forEach((k, v) -> {
                final String infoDetail = "history";
                updateSql.printf((ADD_COLUMN) + "%n", k, infoDetail);
                setJson.printf((EMPTY_COLUMNS_2_JSON) + "%n", k, infoDetail);
//            json字段生成
                final String addJson = v.stream()
                        .map(str -> String.format(JSON_SET, infoDetail, infoDetail, str, str))
                        .collect(joining(","))
                        .replaceAll("\\[", "(").replaceAll("]", ")");
                setJson.println(String.format(UPDATE_SET, k) + addJson + ";");
//            原字段删除
                final String drop = v.stream()
//                        删除的sql中需要保留的字段
                        .filter(str -> !OTHER.contains(str))
//                        删除的sql需要保留的字段
                        .map(str -> String.format(DROP, str))
                        .collect(joining(","))
                        .replaceAll("\\[", "").replaceAll("]", "");
                dropSql.println(String.format(ALTER_TABLE, k) + drop + ";");
            });
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
     * 注入Statement,获取执行结果
     *
     * @param fun:被注入Statement的方法体
     * @return java.util.Optional<T> 执行结果
     * @Author: shiki
     * @Date: 2020/10/28 下午4:02
     */
    static <T> Optional<T> clearConn(Function<Statement, T> fun) {
        try (Connection connection = JdbcUtil.getClearConnection();
             Statement statement = connection.createStatement()) {
            return Optional.ofNullable(fun.apply(statement));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    static void insert() {
//        final List<String> userId = getFile("updateUserId.sql");
        final List<String> userId = Collections.emptyList();
//        解析sql,生成映射实体
        final Map<String, List<SingletonMapRule>> userId2new = userId.stream()
                .filter(ObjectUtils::isNotEmpty)
                .map(str -> {
                    try {
                        str = str.toLowerCase();
                        final String[] adds = str.split(" add ");
                        final String[] afters = adds[1].split(" after ");
                        return SingletonMapRule.builder()
                                .newTableName(adds[0].split(" table ")[1].trim())
                                .oldColumn(SEMICOLON.matcher(afters[1].trim()).replaceAll(""))
                                .newColumn(afters[0].split("column ")[1].split(" bigint ")[0].trim())
                                .newColumnJavaType("long")
                                .newDbDefault("null")
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .peek(s -> s.oldTableName(s.newTableName()))
                .collect(groupingBy(SingletonMapRule::oldTableName));
        /**
         * 获取sql变更脚本 详情见{@link CHAN_DAO}
         */
        final var stringListMap = GenerateFun.updateDbByMd();
        Map<String, List<SingletonMapRule>> map = new HashMap<>(userId2new);
        stringListMap.forEach((k, v) -> map.computeIfAbsent(k, v1 -> new ArrayList<>())
                .addAll(v.stream().map(GenerateFun.Entity::toSingletonMapRule).collect(toList())
                ));
        map.putAll(MergeRule.check.stream().collect(groupingBy(SingletonMapRule::newTableName)));
        final Optional<Map<String, String>> optional = SqlFun.query(PRIMARY, resultSet -> {
            Map<String, String> primary = new HashMap<>();
            try {
                while (resultSet.next()) {
                    primary.put(resultSet.getString("table_name"), resultSet.getString("column_name"));
                }
                return primary;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new RuntimeException();
        });
        final Map<String, String> query = optional.orElseGet(HashMap::new);
//        INSERT into singleton_map_rule(
//        old_table_name, new_table_name, old_column, new_column, new_column_java_type, new_db_default, json_key, old_main_id_name, new_main_id_name)
        List<SQLException> list = new ArrayList<>();
        final List<String> excludeTableName = MergeRule.check.stream().map(SingletonMapRule::oldTableName).distinct().collect(toList());

        SqlFun.insert(insert_singleton_map_rule, stat -> {
            try {
                map.forEach((k, v) -> v.stream()
                        .distinct()
                        .peek(s -> s.newTableName(DELIMITER.matcher(s.newTableName()).replaceAll("").trim()))
                        .filter(s -> !(s.oldColumn().equals("create_user_id") || excludeTableName.contains(s.newTableName())))
                        .forEach(s -> {
                            try {
                                final String oldTableName = DELIMITER.matcher(s.oldTableName()).replaceAll("").trim();
                                final String oldColumn = DELIMITER.matcher(s.oldColumn()).replaceAll("");
                                final String newColumn = DELIMITER.matcher(s.newColumn()).replaceAll("").trim();
                                stat.setString(1, oldTableName);
                                stat.setString(2, DELIMITER.matcher(s.newTableName()).replaceAll("").trim());
                                stat.setString(3, oldColumn.trim());
                                stat.setString(4, oldColumn.equals(newColumn) ? excludeTableName.contains(s.oldTableName()) ? newColumn : newColumn + "_swap" : newColumn);
                                stat.setString(5, MergeRule.mysqlType2javaType(s.newColumnJavaType()).trim());
                                stat.setString(6, s.newDbDefault() == null ? "null" : s.newDbDefault().trim());
                                stat.setString(7, oldColumn.equals(newColumn) ? null : oldColumn);
                                stat.setString(8, s.oldMainIdName() == null ? query.get(oldTableName) == null ? "find not primary" : query.get(oldTableName) : s.oldMainIdName().trim());
                                stat.setString(9, s.newMainIdName() == null ? query.get(oldTableName) == null ? "find not primary" : query.get(oldTableName) : s.newMainIdName().trim());
                                stat.setBoolean(10, !oldColumn.equals(newColumn));
                                stat.setInt(11, s.tableType() == null ? 1 : s.tableType());
                                stat.addBatch();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                list.add(e);
                                throw new RuntimeException(e);
                            }
                        }));
                return stat.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                list.add(e);
                throw new RuntimeException(e);
            }
        });
        list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        final long start = currentTimeMillis();
        EXECUTOR.submit(LineAllIsEmpty::outTableFilterSql);
//        EXECUTOR.submit(LineAllIsEmpty::insert);

        EXECUTOR.shutdown();
        System.out.println("-- 全部执行完毕,消耗总时长" + (currentTimeMillis() - start) + "毫秒");
    }

}