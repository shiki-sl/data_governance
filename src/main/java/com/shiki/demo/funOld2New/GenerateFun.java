package com.shiki.demo.funOld2New;

import com.shiki.demo.constants.BaseConstants;
import com.shiki.demo.fun.Fun;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.shiki.demo.constants.BaseConstants.*;
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

    final static ChanDao CHAN_DAO = initChandao();
    final static Pattern mysqlSemicolon = Pattern.compile("`");

    /**
     * 原库到clear库新增的字段
     *
     * @Author: shiki
     * @Date: 2020/11/16 下午2:38
     */
    static final String CLEAR_ADD_COLUMN = OLD_CLEAR + "3_add_column_to_change_type.sql";
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

    private static final String _6_COPY_RENAME_COLUMN_TO_JSON = OLD_CLEAR + "6_copy_rename_column_to_json.sql";


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
    static <T> T getFile(String fileName, Function<BufferedReader, T> fun) {
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

    static <T> T getNetwork(ChanDao chanDao, Function<BufferedReader, T> fun) {
        Process proc = null;
        try {
            final String path = GenerateFun.class.getClassLoader().getResource("py/chandao.py").getPath();
            proc = Runtime.getRuntime().exec(new String[]{"python", path, chanDao.username, chanDao.password, chanDao.mdUrl});
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                return fun.apply(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                proc.waitFor();
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
                tableName = split[split.length - 1];
                map.computeIfAbsent(tableName, k -> new ArrayList<>());
            }
            final String[] split = str.split(" ");
//            CHANGE COLUMN `attribute4` `is_valid` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '表示是否有效（重要）1-有效  0-无效' FIRST,
            final String[] comments = str.split("'");
            String comment = comments.length > 1 ? comments[1] : "无注释------------------------------";
            if (str.startsWith("CHANGE COLUMN")) {
                map.computeIfAbsent(tableName, k -> new ArrayList<>()).add(new ChangeColumn(tableName, split[2], split[3], split[4], comment));
            }
//            MODIFY COLUMN `is_attendance` tinyint(1) NULL DEFAULT NULL COMMENT '是否列席 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `committee_type`;
            if (str.startsWith("MODIFY COLUMN")) {
                map.computeIfAbsent(tableName, k -> new ArrayList<>()).add(new ModifyColumn(tableName, split[2], split[3], comment));
            }
        }
        return map;
    }

    //    ALTER TABLE `ccxi_dev`.`base_associated_document`
//    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
    private static List<ChangeColumn> generatorUpdateSqlCode(Map<String, List<Entity>> map) {
        try (PrintStream add = new PrintStream(new File(CLEAR_ADD_COLUMN));
             PrintStream del = new PrintStream(new File(CLEAR_DEL_COLUMN));
             PrintStream modify = new PrintStream(new File(CLEAR_MODIFY_COLUMN))
        ) {
            final LinkedList<String> addList = new LinkedList<>();
            final LinkedList<ChangeColumn> setJsonList = new LinkedList<>();
            final LinkedList<String> modifyList = new LinkedList<>();
            final LinkedList<String> setList = new LinkedList<>();
            final List<String> delList = new LinkedList<>();
            map.forEach((tableName, columnNames) -> {
                addList.add("ALTER TABLE " + tableName);
                modifyList.add("ALTER TABLE " + tableName);
                final var columnMaps = columnNames.stream().collect(groupingBy(Entity::getClass));
                columnMaps.getOrDefault(ChangeColumn.class, new ArrayList<>()).forEach(c -> {
                    final ChangeColumn column = (ChangeColumn) c;
//                    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
                    final boolean isBoolean = column.columnType.equalsIgnoreCase("");
                    addList.add("ADD COLUMN " + column.newColumn + " " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 0 " : " NULL COMMENT '" + column.columnComment + "',"));
                    delList.add("alter table " + tableName + " drop column " + column.oldColumn + ";");
//                    UPDATE `singleton_map_rule` SET new_column=new_table_name
                    setList.add("UPDATE " + column.tableName + " SET " + column.newColumn + "=" + column.oldColumn + "");
                    setJsonList.add(column);
                });
                columnMaps.getOrDefault(ModifyColumn.class, new ArrayList<>()).forEach(c -> {
                    final ModifyColumn column = (ModifyColumn) c;
//                    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
                    final boolean isBoolean = column.columnType.equalsIgnoreCase("is_valid");
                    final String oldColumn = mysqlSemicolon.matcher(column.oldColumn).replaceAll("");
                    addList.add("ADD COLUMN `" + oldColumn + "_swap` " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 1 " : " NULL COMMENT '" + column.columnComment + "',"));
                    delList.add("alter table " + tableName + " drop column " + column.oldColumn + "; \n");
                    setList.add("UPDATE " + column.tableName + " SET " + oldColumn + "_swap " + "=" + column.oldColumn + "");
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
            setList.forEach(add::println);
            add.println("SET FOREIGN_KEY_CHECKS = 1;");
            del.println("SET FOREIGN_KEY_CHECKS = 1;");
            modify.println("SET FOREIGN_KEY_CHECKS = 1;");
            return setJsonList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
                    columnGroupingByTableName.computeIfAbsent(split[1], k -> new ArrayList<>()).add("`" + split[2].trim() + "`");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return columnGroupingByTableName;
        });
//    UPDATE `business_transfer_path` SET history=JSON_SET(history, "$.usable_status", usable_status) ,history=JSON_SET(history, "$.create_time", create_time);
        Fun.out(_6_COPY_RENAME_COLUMN_TO_JSON,
                out -> map.forEach((k, v) -> {
                    out.print("UPDATE " + k + " SET ");
                    final String collect = v.stream()
                            .map(str -> mysqlSemicolon.matcher(str).replaceAll(""))
                            .map(column -> "history=JSON_SET(history, \"$." + column + "\", " + column + ")")
                            .collect(joining(","));
                    out.println(collect + ";");
                }));
    }

    public static void main(String[] args) {
        final List<String> list = getNetwork(CHAN_DAO, getAllSqlByMd);
        final Map<String, List<Entity>> map = getAllChangeColumnBySql(list);
        final List<ChangeColumn> changeColumns = generatorUpdateSqlCode(map);
        assert changeColumns != null;
        generatorSetJsonSqlCode(changeColumns);
    }

    public interface Entity {
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
