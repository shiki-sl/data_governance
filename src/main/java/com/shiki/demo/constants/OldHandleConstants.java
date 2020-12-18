package com.shiki.demo.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @Author shiki
 * @description: 旧表处理常量接口
 * @Date 2020/11/13 下午5:39
 */
public interface OldHandleConstants extends BaseConstants {

    /**
     * 特殊字段
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午3:18
     */
    List<String> INVALID_COLUMN = Arrays.asList(
//            "usable_status",
            "create_time",
            "create_ip",
            "create_user_id",
            "create_user_name",
            "modify_time",
            "modify_ip",
            "modify_user_id",
            "modify_user_name",
            "delete_time",
            "delete_ip",
            "delete_flag",
            "delete_user_id",
            "delete_user_name"
//            ,"order_code",
//            "attribute1",
//            "attribute2",
//            "attribute3",
//            "attribute4",
//            "attribute5"
    );

    /**
     * 更新表
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午4:43
     */
    String ALTER_TABLE = "ALTER TABLE %s";

    /**
     * 删除字段
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午4:43
     */
    String DROP = " DROP COLUMN `%s`";

    /**
     * 添加新行
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午3:03
     */
    String ADD_COLUMN = "alter table %s add column %s json comment \"存放老库数据，新库不对其查询、操作，保持老库到新库同步\";";

    /**
     * 生成Json字段
     * 使用说明 str=String.format(EMPTY_COLUMNS_2_JSON, "user","info_detail");
     * str = UPDATE user SET info_detail =JSON_OBJECT()
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午11:03
     */
    String EMPTY_COLUMNS_2_JSON = "UPDATE %s SET %s =JSON_OBJECT();";

    /**
     * update set语句
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午1:56
     */
    String UPDATE_SET = "UPDATE `%s` SET ";

    /**
     * json set方法
     * str=String.format(JSON_SET, "info_detail" ,"age");
     * info_detail=JSON_SET(info_detail, "$.age", age)
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午1:57
     */
    String JSON_SET = "%s=JSON_SET(%s, \"$.%s\", %s) ";

    /**
     * 映射表插入数据
     *
     * @Author: shiki
     * @Date: 2020/11/30 下午3:27
     */
    String INSERT_SINGLETON_MAP_RULE = " INSERT into singleton_map_rule(old_table_name, new_table_name, old_column, new_column, new_column_java_type,\n" +
            "                               new_db_default, json_key, old_main_id_name, new_main_id_name, is_backup, table_type, is_json)\n" +
            " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    /**
     * 查询全部表主键
     *
     * @Author: shiki
     * @Date: 2020/11/30 下午3:28
     */
    String PRIMARY = "select table_name , column_name from  INFORMATION_SCHEMA.KEY_COLUMN_USAGE  t where t.table_schema='ccxi_crc_proj' and t.CONSTRAINT_name = 'PRIMARY'";

    /**
     * 要删除的无效表
     *
     * @Author: shiki
     * @Date: 2020/12/4 上午11:27
     */
    String INVALID = "select table_name from information_schema.tables where TABLE_COMMENT like '%无效表%' and TABLE_SCHEMA = 'ccxi_crc_proj'";

    /**
     * 需要history字段的表
     *
     * @Author: shiki
     * @Date: 2020/12/4 下午2:29
     */
    String INVALID_TABLE_SQL = "select TABLE_NAME, group_concat(COLUMN_NAME) column_names\n" +
            "from information_schema.COLUMNS c\n" +
            "where TABLE_SCHEMA = 'ccxi_crc_proj'\n" +
            "  and c.COLUMN_NAME  not in ('usable_status','create_time')\n" +
            "  and (c.COLUMN_COMMENT like '%无效字段%' or c.COLUMN_COMMENT like '%待处理字段%' or\n" +
            "       c.COLUMN_NAME in\n" +
            "       (select old_column from `rms_clear_rule`.`all_table_map_rule` where old_column not like 'attribute%')\n" +
            "      or c.COLUMN_NAME in ('modify_ip','modify_time','modify_user_id','modify_user_name','create_ip','create_user_id')\n" +
            "      )\n" +
            "  and exists(select *\n" +
            "             from information_schema.TABLES t\n" +
            "             where TABLE_SCHEMA = 'ccxi_crc_proj'\n" +
            "               and t.TABLE_COMMENT not like '%无效表%'\n" +
            "               and t.TABLE_NAME = c.TABLE_NAME)\n" +
            "group by TABLE_NAME";

    /**
     * 获取全部字段注释
     *
     * @Author: shiki
     * @Date: 2020/12/4 下午5:56
     */
    String COLUMN_COMMENT = "select TABLE_NAME, column_name, COLUMN_COMMENT\n" +
            "from information_schema.COLUMNS c\n" +
            "where TABLE_SCHEMA = 'ccxi_crc_proj'\n" +
            "  and exists(select *\n" +
            "             from information_schema.TABLES t\n" +
            "             where TABLE_SCHEMA = 'ccxi_crc_proj'\n" +
            "               and t.TABLE_TYPE != 'VIEW'\n" +
            "               and t.TABLE_NAME = c.TABLE_NAME)";

    /**
     * 数据库的异常字段
     *
     * @Author: shiki
     * @Date: 2020/12/10 下午4:50
     */
    String EXCEPTION_COLUMN = "select table_name, column_name from information_schema.columns where column_comment like '%异常字段%' and table_schema = 'ccxi_crc_proj'";

    /**
     * 等待合并表
     *
     * @Author: shiki
     * @Date: 2020/12/10 下午7:06
     */
    String MERGE_TABLE = "select table_name from information_schema.tables where table_schema = 'ccxi_crc_proj' and TABLE_COMMENT like '%待合并表%'";
}
