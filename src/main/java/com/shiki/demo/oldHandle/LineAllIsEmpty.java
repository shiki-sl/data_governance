package com.shiki.demo.oldHandle;

import com.shiki.demo.constants.BaseConstants;
import com.shiki.demo.constants.MergeRule;
import com.shiki.demo.fun.SqlFun;
import com.shiki.demo.funOld2New.GenerateFun;
import com.shiki.demo.jdbc.config.JdbcUtil;
import com.shiki.demo.movedb.SingletonMapRule;
import lombok.var;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.shiki.demo.constants.BaseConstants.*;
import static com.shiki.demo.constants.OldHandleConstants.*;
import static com.shiki.demo.funOld2New.GenerateFun.getChanDaoByNetwork;
import static com.shiki.demo.funOld2New.GenerateFun.updateDbByMd;
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
 *
 * <p>
 * @see BaseConstants#ROOT_PATH 文件输出目录,需要保证该位置是一个文件夹,会在文件夹下生成独立文件,修改请见{@link #_1_COPY_INVALID_COLUMN_TO_JSON},
 * {@link #_2_DEL_INVALID_COLUMN} 独立文件详情见{@link BaseConstants#ROOT_PATH} 上方字段注释
 *
 * <p>
 * {@link com.shiki.demo.jdbc.config.DBPool#db} 新库名
 */
public class LineAllIsEmpty {

    final static Pattern DELIMITER = Pattern.compile("`");

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
     */

    static final String _1_COPY_INVALID_COLUMN_TO_JSON = OLD_CLEAR + "3_copy_invalid_column_to_json.sql";
    static final String _1_SET_JSON = OLD_CLEAR_COLUMN_UPDATE + "1_set_json.sql";
    static final String _2_DEL_INVALID_COLUMN = CLEAR_DEV + "2_del_invalid_column.sql";

    /**
     * 获取全部无效表
     *
     * @Author: shiki
     * @Date: 2020/12/4 下午5:57
     */
    public static final List<String> INVALID_TABLE = conn(stat -> {
        List<String> invalid_table = new ArrayList<>();
        try (final ResultSet resultSet = stat.executeQuery(INVALID)) {
            while (resultSet.next()) {
                invalid_table.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invalid_table;
    }).orElseGet(ArrayList::new);


    /**
     * @Author: shiki
     * @Date: 2020/12/4 下午5:54
     */
    public static final Map<String, Map<String, String>> COLUMN_COMMENT_MAP = conn(stat -> {
        final Map<String, Map<String, String>> map = new HashMap<>();
        SqlFun.query(COLUMN_COMMENT, resultSet -> {
            try {
                while (resultSet.next()) {
                    map.computeIfAbsent(resultSet.getString("table_name"),
                            v -> new HashMap<>()).put(resultSet.getString("column_name"), resultSet.getString("column_comment"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return map;
        });
        return map;
    }).orElseGet(HashMap::new);

    /**
     * 数据库异常字段
     *
     * @Author: shiki
     * @Date: 2020/12/10 下午7:06
     */
    public static final Map<String, List<String>> EXCEPTION_COLUMNS = conn(stat -> {
        final Map<String, List<String>> map = new HashMap<>();
        SqlFun.query(EXCEPTION_COLUMN, resultSet -> {
            try {
                while (resultSet.next()) {
                    final String tableName = resultSet.getString("table_name");
                    final String columnName = resultSet.getString("column_name");
                    map.computeIfAbsent(tableName, k-> new ArrayList<>()).add(columnName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return map;
        });
        return map;
    }).orElseGet(HashMap::new);

    /**
     * 待合并的表
     *
     * @Author: shiki
     * @Date: 2020/12/10 下午7:07
     */
    public static final List<String> MERGE_TABLES = conn(stat -> {
        final List<String> list = new ArrayList<>();
        getChanDaoByNetwork(GenerateFun.CHAN_DAO, reader -> {
            String str;
            String strCopy = "";
            try {
                while ((str = reader.readLine()) != null) {
                    if (str.contains("--------------")) {
                        final String s = strCopy.split("\\.")[1].trim();
                        final String e = s.split(" ")[0];
                        list.add(e);
                    }
                    strCopy = str;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        });
        return list.stream().distinct().collect(toList());
    }).orElseGet(ArrayList::new);

    static final Map<String, List<String>> INVALID_MAP = conn(stat -> {
        final Map<String, List<SingletonMapRule>> mapByMd = updateDbByMd();
        final Map<String, List<String>> map = new HashMap<>(72);
        try (final ResultSet resultSet = stat.executeQuery(INVALID_TABLE_SQL)) {
            while (resultSet.next()) {
                map.put(resultSet.getString("table_name"), new ArrayList<>(Arrays.asList(resultSet.getString("column_names").split(","))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mapByMd.forEach((k, v) -> {
            final List<String> list = v.stream()
                    .map(SingletonMapRule::oldColumn)
                    .collect(toList());
            map.computeIfAbsent(k, v1 -> new ArrayList<>()).addAll(list);
        });
        return map;
    }).orElseGet(HashMap::new);

    /**
     * 输出可执行sql和空字段集合到本地文件
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午4:58
     */
    static void outTableFilterSql() {
        //        尝试设置输出位置,生成文件
        try (final PrintStream updateSql = new PrintStream(new File(_1_COPY_INVALID_COLUMN_TO_JSON));
             final PrintStream dropSql = new PrintStream(new File(_2_DEL_INVALID_COLUMN));
             final PrintStream setJson = new PrintStream(new File(_1_SET_JSON))) {
//            删除合并表
            MERGE_TABLES.forEach(INVALID_MAP::remove);
//
            System.out.println(INVALID_MAP.get("business_check_opinion_big_record"));
            EXCEPTION_COLUMNS.forEach((k, v) ->
                    INVALID_MAP.computeIfPresent(k, (k1, v1) ->
                            v1.stream().filter(item -> !v.contains(item)).collect(toList()))
            );
            System.out.println(INVALID_MAP.get("business_check_opinion_big_record"));
            INVALID_MAP.forEach((k, v) -> {
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
                        .distinct()
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

    static void insert() {
        final var stringListMap = updateDbByMd();
        Map<String, List<SingletonMapRule>> map = new HashMap<>();
        stringListMap.forEach((k, v) -> map.computeIfAbsent(k, v1 -> new ArrayList<>()).addAll(v));
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
        });
        final Map<String, String> query = optional.orElseGet(HashMap::new);
//        INSERT into singleton_map_rule(
//        old_table_name, new_table_name, old_column, new_column, new_column_java_type, new_db_default, json_key, old_main_id_name, new_main_id_name)
        List<SQLException> list = new ArrayList<>();
        final List<String> excludeTableName = MergeRule.check.stream().map(SingletonMapRule::oldTableName).distinct().collect(toList());

        SqlFun.insertOrBatch(INSERT_SINGLETON_MAP_RULE, stat -> {
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
                                stat.setString(7, ObjectUtils.isNotEmpty(s.jsonKey()) ? s.jsonKey() : oldColumn.equals(newColumn) ? null : oldColumn);
                                stat.setString(8, s.oldMainIdName() == null ? query.get(oldTableName) == null ? "find not primary" : query.get(oldTableName) : s.oldMainIdName().trim());
                                stat.setString(9, s.newMainIdName() == null ? query.get(oldTableName) == null ? "find not primary" : query.get(oldTableName) : s.newMainIdName().trim());
                                stat.setBoolean(10, ObjectUtils.isEmpty(s.jsonKey()) && !oldColumn.equals(newColumn));
                                stat.setInt(11, s.tableType() == null ? 1 : s.tableType());
                                stat.setBoolean(12, s.isJson() != null && s.isJson());
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