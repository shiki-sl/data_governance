package com.shiki.demo.funOld2New;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

import static com.shiki.demo.constants.BaseConstants.ROOT_PATH;
import static java.util.stream.Collectors.groupingBy;

/**
 * @Author shiki
 * @description: 读取数据库，生成旧库到新库的映射sql
 * @Date 2020/11/13 下午5:13
 */
public class GenerateFun {

    /**
     * 原库到clear库新增的字段
     *
     * @Author: shiki
     * @Date: 2020/11/16 下午2:38
     */
    static final String CLEAR_ADD_COLUMN = ROOT_PATH + "clear_add_column.sql";
    /**
     * clear库到dev删除的字段
     *
     * @Author: shiki
     * @Date: 2020/11/16 下午2:38
     */
    static final String CLEAR_DEL_COLUMN = ROOT_PATH + "dev_del_column.sql";
    /**
     * 临时变量重命名
     *
     * @Author: shiki
     * @Date: 2020/11/17 上午10:23
     */
    private static final String CLEAR_MODIFY_COLUMN = ROOT_PATH + "dev_modify_column.sql";

    /**
     * 读取resource文件下面的update_db文件中的全部code,要求其中代码部分全部是sql代码
     *
     * @return java.util.List<java.lang.String>
     * @Author: shiki
     * @Date: 2020/11/16 上午11:26
     */
    static List<String> getAllSqlByMd() {
        final List<String> list = new ArrayList<>();
        URI resource;
        try {
            resource = Objects.requireNonNull(GenerateFun.class.getClassLoader().getResource("update_db")).toURI();
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(resource)))) {
                String str;
//                解析md文件中的代码
                boolean start = "```".equalsIgnoreCase(reader.readLine());
                while ((str = reader.readLine()) != null) {
                    if (str.equalsIgnoreCase("```")) {
                        start = !start;
                        continue;
                    }
                    if (start) {
                        list.add(str);
                    }
                }
                return list;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
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
                map.computeIfAbsent(tableName, k -> new ArrayList<>()).add(new ChangeColumn(split[2], split[3], split[4], comment));
            }
//            MODIFY COLUMN `is_attendance` tinyint(1) NULL DEFAULT NULL COMMENT '是否列席 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `committee_type`;
            if (str.startsWith("MODIFY COLUMN")) {
                map.computeIfAbsent(tableName, k -> new ArrayList<>()).add(new ModifyColumn(split[2], split[3], comment));
            }
        }
        return map;
    }

    //    ALTER TABLE `ccxi_dev`.`base_associated_document`
//    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
    private static List<String> generatorSqlCode(Map<String, List<Entity>> map) {
        try (PrintStream add = new PrintStream(new File(CLEAR_ADD_COLUMN));
             PrintStream del = new PrintStream(new File(CLEAR_DEL_COLUMN));
             PrintStream modify = new PrintStream(new File(CLEAR_MODIFY_COLUMN))
        ) {
            final Pattern compile = Pattern.compile("`");
            final LinkedList<String> addList = new LinkedList<>();
            final LinkedList<String> modifyList = new LinkedList<>();
            final List<String> delList = new LinkedList<>();
            map.forEach((tableName, columnNames) -> {
                addList.add("ALTER TABLE `ccxi_crc_proj_clear`." + tableName);
                modifyList.add("ALTER TABLE `ccxi_crc_proj_clear`." + tableName);
                final var columnMaps = columnNames.stream().collect(groupingBy(Entity::getClass));
                columnMaps.getOrDefault(ChangeColumn.class, new ArrayList<>()).forEach(c -> {
                    final ChangeColumn column = (ChangeColumn) c;
//                    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
                    final boolean isBoolean = column.columnType.equalsIgnoreCase("");
                    addList.add("ADD COLUMN " + column.newColumn + " " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 0 " : " NULL COMMENT '" + column.columnComment + "',"));
                    delList.add("alter table " + tableName + " drop column " + column.oldColumn + ";");
                });
                columnMaps.getOrDefault(ModifyColumn.class, new ArrayList<>()).forEach(c -> {
                    final ModifyColumn column = (ModifyColumn) c;
//                    ADD COLUMN `is_operation_material_swap` tinyint(1) NULL COMMENT '是否运作材料 $RM_YES_NOT=否、是 {0=否,1=是}' AFTER `history`;
                    final boolean isBoolean = column.columnType.equalsIgnoreCase("is_valid");
                    final String oldColumn = compile.matcher(column.oldColumn).replaceAll("");
                    addList.add("ADD COLUMN `" + oldColumn + "_swap` " + column.columnType + " " + (isBoolean ? " NOT NULL DEFAULT 1 " : " NULL COMMENT '" + column.columnComment + "',"));
                    delList.add("alter table " + tableName + " drop column " + column.oldColumn + "; \n");
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
            add.println("SET FOREIGN_KEY_CHECKS = 1;");
            del.println("SET FOREIGN_KEY_CHECKS = 1;");
            modify.println("SET FOREIGN_KEY_CHECKS = 1;");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        final List<String> list = getAllSqlByMd();
        final Map<String, List<Entity>> map = getAllChangeColumnBySql(list);
        generatorSqlCode(map);
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
        String oldColumn;
        String newColumn;
        String columnType;
        String columnComment;
    }
}
