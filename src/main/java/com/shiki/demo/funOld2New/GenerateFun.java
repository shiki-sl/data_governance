package com.shiki.demo.funOld2New;

import com.shiki.demo.fun.Fun;
import com.shiki.demo.movedb.SingletonMapRule;
import com.shiki.demo.oldHandle.LineAllIsEmpty;
import io.vavr.Tuple2;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.shiki.demo.constants.BaseConstants.*;
import static com.shiki.demo.oldHandle.LineAllIsEmpty.*;
import static java.util.stream.Collectors.*;

/**
 * @Author shiki
 * @description: 读取数据库，生成旧库到新库的映射sql
 * @Date 2020/11/13 下午5:13
 */
public class GenerateFun {

    static {
        init();
    }

    private static void init() {
        System.out.println("BaseConstants init is " + isInit);
    }

    /**
     * 获取禅道配置信息
     *
     * @return com.shiki.demo.funOld2New.GenerateFun.ChanDao
     * @Author: shiki
     * @Date: 2020/12/1 下午6:05
     */
    static ChanDao initChandao() {
        final InputStream is = GenerateFun.class.getResourceAsStream("/application.properties");
        try {
            Properties prop = new Properties();
            prop.load(is);
            return new ChanDao(prop.getProperty("loginUrl"), prop.getProperty("chandao_username"),
                    prop.getProperty("chandao_password"), prop.getProperty("mdUrl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("init exception");
    }

    /**
     * 禅道
     *
     * @Author: shiki
     * @Date: 2020/12/1 下午6:06
     */
    public final static ChanDao CHAN_DAO = initChandao();

    /**
     * mysql中的 '`' 标识符
     *
     * @Author: shiki
     * @Date: 2020/12/1 下午6:05
     */
    final static Pattern mysqlSemicolon = Pattern.compile("`");

    /**
     * 原库到clear库新增的字段
     *
     * @Author: shiki
     * @Date: 2020/11/16 下午2:38
     */
    static final String CLEAR_ADD_COLUMN = OLD_CLEAR + "2_add_column_to_change_type.sql";
    /**
     * 原库到clear库新增的字段
     *
     * @Author: shiki
     * @Date: 2020/11/16 下午2:38
     */
    static final String COLUMN_TYPE_UPDATE_SET_SWAP_COLUMN = OLD_CLEAR_COLUMN_UPDATE + "5_column_type_update_set_swap_column.sql";
    /**
     * clear库到dev删除的字段
     *
     * @Author: shiki
     * @Date: 2020/11/16 下午2:38
     */
    static final String CLEAR_DEL_COLUMN = CLEAR_DEV + "3_del_rename_old_column.sql";
    /**
     * 临时变量重命名
     *
     * @Author: shiki
     * @Date: 2020/11/17 上午10:23
     */
    private static final String CLEAR_MODIFY_COLUMN = CLEAR_DEV + "4_rename_swap_column.sql";

    private static final String _2_COPY_RENAME_COLUMN_TO_JSON = OLD_CLEAR_COLUMN_UPDATE + "2_copy_rename_column_to_json.sql";


    static Function<BufferedReader, List<String>> getAllSqlByMd = reader -> {
        final List<String> list = new ArrayList<>();
        String str;
//        解析md文件中的代码
        boolean start;
        try {
            start = "```".equalsIgnoreCase(reader.readLine());
            while ((str = reader.readLine()) != null) {
                if (str.equalsIgnoreCase("```")) {
                    start = !start;
                    continue;
                }
                if (start) {
                    list.add(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    };

    /**
     * 读取resource文件下面的update_db文件中的全部code,要求其中代码部分全部是sql代码
     *
     * @return java.util.List<java.lang.String>
     * @Author: shiki
     * @Date: 2020/11/16 上午11:26
     */
    public static <T> T getFile(String fileName, Function<BufferedReader, T> fun) {
        URI resource;
        try {
            resource = Objects.requireNonNull(GenerateFun.class.getClassLoader().getResource(fileName)).toURI();
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(resource)))) {
                return fun.apply(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("处理失败");
    }

    public static List<String> getFile(String fileName) {
        List<String> list = new LinkedList<>();
        URI resource;
        try {
            resource = Objects.requireNonNull(GenerateFun.class.getClassLoader().getResource(fileName)).toURI();
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(resource)))) {
                String str;
                while ((str = reader.readLine()) != null) {
                    list.add(str);
                }
                return list;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("处理失败");
    }

    /**
     * 获取线上禅道文档
     *
     * @param chanDao:
     * @param fun:
     * @return T
     * @Author: shiki
     * @Date: 2020/12/1 下午6:06
     */
    public static <T> T getChanDaoByNetwork(ChanDao chanDao, Function<BufferedReader, T> fun) {
        Process proc;
        try {
            final String path = GenerateFun.class.getClassLoader().getResource("py/chan_dao.py").getPath();
            final String[] cmdArray = {"python", path, chanDao.username, chanDao.password, chanDao.loginUrl, chanDao.mdUrl};
            proc = Runtime.getRuntime().exec(cmdArray);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                return fun.apply(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (proc.waitFor() != 0) {
                    throw new RuntimeException("python 执行错误");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("处理失败");
    }

    /**
     * 根据读出的sql代码将其格式化并保存到实体 {@link Entity}
     *
     * @param list:
     * @return java.util.Map<java.lang.String, java.util.List < com.shiki.demo.funOld2New.GenerateFun.Entity>>
     * @Author: shiki
     * @Date: 2020/11/16 上午11:27
     */
    private static Map<String, List<Entity>> getAllChangeColumnBySql(List<String> list) {
        final HashMap<String, List<Entity>> map = new HashMap<>();
        String tableName = "empty";
        for (String str : list) {
            if (str.startsWith("ALTER TABLE")) {
                final String[] split = str.split("\\.");
                tableName = mysqlSemicolon.matcher(split[split.length - 1].split("ALTER TABLE")[1].trim()).replaceAll("");
                map.computeIfAbsent(tableName, k -> new ArrayList<>());
            }
            final String[] split = str.split(" ");
//            CHANGE COLUMN `attribute4` `is_valid` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '表示是否有效（重要）1-有效  0-无效' FIRST,
            Map<String, String> tableNames = COLUMN_COMMENT_MAP.get(tableName);
            if (str.startsWith("CHANGE COLUMN")) {
                final String key = mysqlSemicolon.matcher(split[2]).replaceAll("");
                String comment = tableNames.get(key);
                map.computeIfAbsent(tableName, k -> new ArrayList<>()).add(new ChangeColumn(tableName, split[2], split[3], split[4], comment));
            }
//            MODIFY COLUMN `is_attendance` tinyint(1) NULL DEFAULT NULL COMMENT '是否列席 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `committee_type`;
            if (str.startsWith("MODIFY COLUMN")) {
                final String key = mysqlSemicolon.matcher(split[2]).replaceAll("");
                String comment = tableNames.get(key);
                map.computeIfAbsent(tableName, k -> new ArrayList<>()).add(new ModifyColumn(tableName, split[2], split[3], comment));
            }
        }
        return map;
    }

    //    ALTER TABLE `ccxi_dev`.`base_associated_document`
//    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
    private static void generatorUpdateSqlCode(Map<String, List<Entity>> map) {
        try (PrintStream add = new PrintStream(new File(CLEAR_ADD_COLUMN));
             PrintStream set = new PrintStream(new File(COLUMN_TYPE_UPDATE_SET_SWAP_COLUMN));
             PrintStream del = new PrintStream(new File(CLEAR_DEL_COLUMN));
             PrintStream modify = new PrintStream(new File(CLEAR_MODIFY_COLUMN))
        ) {
            final LinkedList<String> addList = new LinkedList<>();
            final LinkedList<String> modifyList = new LinkedList<>();
            final LinkedList<String> setList = new LinkedList<>();
            final List<String> delList = new LinkedList<>();
            MERGE_TABLES.forEach(map::remove);
            EXCEPTION_COLUMNS.forEach((k, v) ->
                    map.computeIfPresent(k, (k1, v1) -> v1.stream()
                            .filter(item -> !v.contains(mysqlSemicolon.matcher(item.toSingletonMapRule().oldColumn()).replaceAll("")))
                            .collect(toList()))
            );
            map.forEach((tableName, columnNames) -> {
                addList.add("ALTER TABLE " + tableName);
                modifyList.add("ALTER TABLE " + tableName);
                Map<String, String> tableNames = COLUMN_COMMENT_MAP.get(tableName);
                final var columnMaps = columnNames.stream().collect(groupingBy(Entity::getClass));
                columnMaps.getOrDefault(ChangeColumn.class, new ArrayList<>()).forEach(c -> {
                    final ChangeColumn column = (ChangeColumn) c;
//                    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
                    final boolean isBoolean = column.columnType.equalsIgnoreCase("");
                    addList.add("ADD COLUMN " + column.newColumn + " " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 0 " : " NULL COMMENT '" + tableNames.get(mysqlSemicolon.matcher(column.oldColumn).replaceAll("")) + "',"));
                    delList.add("alter table " + tableName + " drop column " + column.oldColumn + ";");
//                    UPDATE `singleton_map_rule` SET new_column=new_table_name
                    setList.add("UPDATE " + column.tableName + " SET " + column.newColumn + "=" + column.oldColumn + ";");
                });
                columnMaps.getOrDefault(ModifyColumn.class, new ArrayList<>()).forEach(c -> {
                    final ModifyColumn column = (ModifyColumn) c;
//                    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
                    final boolean isBoolean = column.columnType.equalsIgnoreCase("is_valid");
                    final String oldColumn = mysqlSemicolon.matcher(column.oldColumn).replaceAll("");
                    addList.add("ADD COLUMN `" + oldColumn + "_swap` " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 1 " : " NULL COMMENT '" + column.columnComment + "',"));
                    delList.add("alter table " + tableName + " drop column " + column.oldColumn + "; \n");
                    setList.add("UPDATE " + column.tableName + " SET " + oldColumn + "_swap " + "=" + column.oldColumn + ";");
//                    CHANGE COLUMN `is_upload_swap` `is_upload` tinyint(1)  NULL COMMENT '是否下载'
//                    CHANGE COLUMN `is_interview_swap` `is_interview_swap1` tinyint(1) NULL DEFAULT NULL COMMENT '是否访谈' AFTER `is_negative_swap`;
                    modifyList.add("CHANGE COLUMN `" + oldColumn + "_swap` `" + oldColumn + "` " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 1 " : " NULL COMMENT '" + column.columnComment + "',"));
                });
                addList.addLast(addList.removeLast().replace(",", ""));
                modifyList.addLast(modifyList.removeLast().replace(",", ""));
                addList.add(";");
                delList.add(";");
                modifyList.add(";");
            });
            addList.forEach(add::println);
            modifyList.forEach(modify::println);
            delList.stream().filter(str -> !str.equals(";")).forEach(del::println);
            setList.forEach(set::println);
            add.println("SET FOREIGN_KEY_CHECKS = 1;");
            del.println("SET FOREIGN_KEY_CHECKS = 1;");
            modify.println("SET FOREIGN_KEY_CHECKS = 1;");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void generatorSetJsonSqlCode(List<ChangeColumn> list) {
        final Map<String, List<String>> columnGroupingByTableName = list.stream()
                .collect(
                        groupingBy(ChangeColumn::getTableName,
                                mapping(ChangeColumn::getOldColumn, toList())
                        )
                );
        final Map<String, List<String>> map = getFile("_del_column.sql", reader -> {
            String str;
//            解析sql文件中的代码
//            alter table `business_meeting` drop column `attribute1`;
            try {
                while ((str = reader.readLine()) != null) {
                    final String[] split = str.split("`");
                    columnGroupingByTableName.computeIfAbsent(split[1], k -> new ArrayList<>()).add("`" + split[3].trim() + "`");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return columnGroupingByTableName;
        });
//    UPDATE `business_transfer_path` SET history=JSON_SET(history, "$.usable_status", usable_status) ,history=JSON_SET(history, "$.create_time", create_time);
        Fun.out(_2_COPY_RENAME_COLUMN_TO_JSON,
                out -> map.forEach((k, v) -> {
                    out.print("");
                    final String collect = v.stream()
                            .filter(str -> !str.contains("attribute"))
                            .map(str -> mysqlSemicolon.matcher(str).replaceAll(""))
                            .map(column -> "history=JSON_SET(history, \"$." + column + "\", " + column + ")")
                            .collect(joining(","));
                    if (ObjectUtils.isNotEmpty(collect)) {
                        out.println("UPDATE " + k + " SET " + collect + ";");
                    }
                }));
    }

    public static Map<String, List<SingletonMapRule>> updateDbByMd() {
        final List<String> list = getChanDaoByNetwork(CHAN_DAO, getAllSqlByMd);
        final Map<String, List<Entity>> sql = getAllChangeColumnBySql(list);
        Map<String, List<SingletonMapRule>> map = new HashMap<>();
        sql.forEach((k, v) ->
                map.put(k, v.stream()
                        .map(Entity::toSingletonMapRule)
                        .peek(s -> s.oldColumn(mysqlSemicolon.matcher(s.oldColumn()).replaceAll("")))
                        .peek(s -> s.newColumn(mysqlSemicolon.matcher(s.newColumn()).replaceAll("")))
                        .collect(toList())));
        return map;
    }

    public static void main(String[] args) {
        final List<String> list = getChanDaoByNetwork(CHAN_DAO, getAllSqlByMd);
        final Map<String, List<Entity>> map = getAllChangeColumnBySql(list);
//        map.forEach((k, v) -> {
//            System.out.println("k = " + k);
//            System.out.println("v = " + v);
//        });
        generatorUpdateSqlCode(map);
    }

    public interface Entity {
        SingletonMapRule toSingletonMapRule();
    }

    /**
     * 更改字段类型
     *
     * @Author: shiki
     * @Date: 2020/11/16 上午11:21
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ModifyColumn implements Entity {
        String tableName;
        String oldColumn;
        String columnType;
        String columnComment;

        @Override
        public SingletonMapRule toSingletonMapRule() {
            return SingletonMapRule.builder()
                    .newTableName(tableName)
                    .oldTableName(tableName)
                    .oldColumn(oldColumn)
                    .newColumn(oldColumn)
                    .newColumnJavaType(columnType)
                    .build();
        }
    }

    /**
     * 字段重命名
     *
     * @Author: shiki
     * @Date: 2020/11/16 上午11:22
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ChangeColumn implements Entity {
        String tableName;
        String oldColumn;
        String newColumn;
        String columnType;
        String columnComment;

        @Override
        public SingletonMapRule toSingletonMapRule() {
            return SingletonMapRule.builder()
                    .newTableName(tableName)
                    .oldTableName(tableName)
                    .oldColumn(oldColumn)
                    .newColumn(newColumn)
                    .newColumnJavaType(columnType)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ChanDao {
        /**
         * 禅道登录地址
         *
         * @Author: shiki
         * @Date: 2020/11/19 下午5:34
         */
        String loginUrl;
        /**
         * 禅道登用户
         *
         * @Author: shiki
         * @Date: 2020/11/19 下午5:34
         */
        String username;
        /**
         * 禅道登录密码
         *
         * @Author: shiki
         * @Date: 2020/11/19 下午5:34
         */
        String password;
        /**
         * md文档爬取url
         *
         * @Author: shiki
         * @Date: 2020/11/19 下午5:35
         */
        String mdUrl;
    }
}
