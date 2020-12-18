package com.shiki.demo.movedb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author shiki
 * @description: 数据库迁移映射
 * @Date 2020/11/23 下午5:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true, fluent = true)
public class SingletonMapRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 旧表名
     */
    private String oldTableName;

    /**
     * 新表名
     */
    private String newTableName;

    /**
     * 映射类型
     */
    private Integer tableType;

    /**
     * 旧库字段名
     */
    private String oldColumn;

    /**
     * 新库字段名
     */
    private String newColumn;

    /**
     * 新表在java中的数据类型
     */
    private String newColumnJavaType;

    /**
     * 监听表主键名称
     */
    private String oldMainIdName;

    /**
     * 同步表主键名称
     */
    private String newMainIdName;

    /**
     * 默认值
     */
    private String newDbDefault;

    /**
     * 转json时的key，保存在新数据库中的列名为history
     */
    private String jsonKey;


    /**
     * new_column是否是json类型
     */
    private Boolean isJson = false;

    /**
     * 是否需要在json和new_column中保留两份数据
     */
    private Boolean isBackup;
}
