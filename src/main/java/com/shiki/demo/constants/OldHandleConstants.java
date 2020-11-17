package com.shiki.demo.constants;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author shiki
 * @description: 旧表处理常量接口
 * @Date 2020/11/13 下午5:39
 */
public interface OldHandleConstants extends BaseConstants {
    /**
     * 已处理的表数量
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:51
     */
    AtomicInteger HANDLE_TABLE = new AtomicInteger(0);

    /**
     * 特殊字段
     *
     * @Author: shiki
     * @Date: 2020/10/27 下午3:18
     */
    List<String> INVALID_COLUMN = Arrays.asList(
            "usable_status",
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
            "delete_user_name",
            "order_code"
//            , "attribute1",
//            "attribute2",
//            "attribute3",
//            "attribute4",
//            "attribute5"
    );

    /**
     * 排除表
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:51
     */
    List<String> INVALID_TABLES = Collections.singletonList(
            "rm_log"
    );

    /**
     * 统计失败的表
     *
     * @Author: shiki
     * @Date: 2020/10/26 下午5:20
     */
    Set<String> FAIL_TABLE_NAME = new HashSet<>(255);

    /**
     * 外键查询结果别名
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午8:30
     */
    String DEL_PK = "del_pk";

    String NEW_DB = "ccxi_crc_proj_dev";

    /**
     * 删除全部外键
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午8:10
     */
    String DROP_PK = " select\n " +
            " concat( 'alter table ', table_name, ' drop foreign key ', constraint_name, ';' ) as " + DEL_PK + "\n " +
            " from\n " +
            " information_schema.table_constraints \n " +
            " where\n " +
            " constraint_type = 'FOREIGN KEY' \n " +
            " and table_schema = '" + "ccxi_crc_proj_combing" + "';";


    /**
     * 删除表
     *
     * @Author: shiki
     * @Date: 2020/10/28 下午5:37
     */
    String DROP_TABLE = "drop  table if exists ";

    /**
     * 查询全部表名
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:50
     */
    String ALL_TABLE_NAME = "select\n" +
            "    TABLE_NAME,\n" +
            "    TABLE_COMMENT\n" +
            "from\n" +
            "    INFORMATION_SCHEMA.Tables\n" +
            "where\n" +
            "        table_schema = '%s'";

    /**
     * 用于替换的动态表列名
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:54
     */
    String DYNAMIC_COLUMN_NAME = "dynamic_column_name";

    /**
     * 用于替换的动态表名
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:55
     */
    String DYNAMIC_TABLE_NAME = "dynamic_table_name";

    /**
     * 查询表的全部列
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:55
     */
    String ALL_COLUMN_NAME = "select distinct column_name from information_schema.columns where table_schema = '%s' and table_name = '%s'";

    /**
     * 查询表字段数
     *
     * @Author: shiki
     * @Date: 2020/10/29 下午3:05
     */
    String TABLE_COLUMN_COLUMN = "select count(*) as count from information_schema.columns where table_schema = '%s' and table_name = '%s'";

    /**
     * 用于标记查询出的列名称
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:57
     */
    String PRE = "select 'column_name',1 'count'";

    /**
     * 查出全部的空列
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:57
     */
    String TABLE_ALL_LINE_IS_NOT_EMPTY = " select 'dynamic_column_name', exists(select 1 from dynamic_table_name where dynamic_column_name is not null and dynamic_column_name != '') 'count' ";

    /**
     * 联表查询关键字
     *
     * @Author: shiki
     * @Date: 2020/10/27 上午10:58
     */
    String UNION = " union ";

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
}
