package com.shiki.demo.constants;

import com.shiki.demo.movedb.SingletonMapRule;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vavr.API.Case;
import static io.vavr.API.Match;

/**
 * @Author shiki
 * @description: 表合并的映射
 * @Date 2020/11/23 下午3:22
 */
public interface MergeRule {

    static String mysqlType2javaType(String mysql) {
//        Assert.isNull(mysql,"不能转换空");
        if (mysql.startsWith("varchar")) return "string";
        if (mysql.startsWith("char")) return "string";
        if (mysql.startsWith("text")) return "string";
        if (mysql.startsWith("json")) return "json";
        if (mysql.equals("tinyint(1)")) return "boolean";
        if (mysql.startsWith("tinyint")) return "byte";
        if (mysql.startsWith("bigint")) return "long";
        if (mysql.startsWith("long")) return "long";
        if (mysql.startsWith("int")) return "int";
        if (mysql.startsWith("date")) return "localdate";
        if (mysql.startsWith("time")) return "localtime";
        if (mysql.startsWith("datetime")) return "localdatetime";
        if (mysql.startsWith("timestamp")) return "localdatetime";
        if (mysql.startsWith("decimal")) return "bigdecimal";
        if (mysql.startsWith("character")) return "bigdecimal";
        throw new RuntimeException("undefined:" + mysql);
    }

    List<SingletonMapRule> check = new ArrayList<SingletonMapRule>() {{
//        rms_project_check
//        one
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("attribute5").newColumn("sub_module").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("attribute4").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("rms_project_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
//        two
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("attribute5").newColumn("sub_module").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("attribute4").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("rms_project_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
//        zpthree
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("attribute5").newColumn("sub_module").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("is_meet").newColumn("is_meet").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("attribute3").newColumn("is_zpthree_check").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("attribute4").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("rms_project_check").oldColumn("no_meeting_reason").newColumn("no_meeting_reason").newColumnJavaType("varchar(1000)").newDbDefault("null").build());
//        three
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("attribute5").newColumn("sub_module").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("attribute4").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("rms_project_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
//        business_notice_review
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("id").oldColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("check_opinion").oldColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("usable_status").oldColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("sub_module").oldColumn("attribute5").newColumnJavaType("varchar(255)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("is_effective").oldColumn("is_effective").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("recorder_name").oldColumn("recorder_name").newColumnJavaType("varchar(255)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("check_date").oldColumn("check_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("operate_id").oldColumn("operate_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("ccxi_user_id").oldColumn("checker_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("check_state").oldColumn("check_state").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("recorder_id").oldColumn("recorder_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_notice_review").newTableName("rms_project_check").newColumn("record_date").oldColumn("record_date").newColumnJavaType("date").newDbDefault("null").build());
//        business_compliance_check
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("id").oldColumn("id").newColumnJavaType("bigint").newDbDefault("not").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("operate_id").oldColumn("operate_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("check_state").oldColumn("check_state").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("check_date").oldColumn("check_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("check_opinion").oldColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("usable_status").oldColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("sub_module").oldColumn("attribute5").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_compliance_check").newTableName("rms_project_check").newColumn("ignore_remind").oldColumn("attribute4").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
//        business_project_operate_end
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("project_id").newColumn("project_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("proposer_id").newColumn("proposer_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("proposer_date").newColumn("proposer_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("is_end_stop_revive").newColumn("is_end_stop_revive").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("auditor_id").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("audit_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("proposer_name").newColumn("proposer_name").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("proposer_explain").newColumn("proposer_explain").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("auditor_name").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("audit_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("attribute4").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_operate_end").newTableName("rms_project_check").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(900)").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("proposer_id").newColumn("proposer_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("proposer_date").newColumn("proposer_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("audit_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("auditor_id").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("proposer_name").newColumn("proposer_name").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("attribute3").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("attribute4").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("attribute5").newColumn("sub_module").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("auditor_name").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("audit_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("proposer_department_id").newColumn("proposer_department_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("auditor_department_id").newColumn("auditor_department_id").newColumnJavaType("bigint").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("proposer_department_name").newColumn("info").isJson(true).jsonKey("proposer_department_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("auditor_department_name").newColumn("info").isJson(true).jsonKey("auditor_department_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("meanwhile_remark").newColumn("info").isJson(true).jsonKey("meanwhile_remark").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("meanwhile_code").newColumn("info").isJson(true).jsonKey("meanwhile_code").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("is_main_upgrade").newColumn("info").isJson(true).jsonKey("is_main_upgrade").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("is_same_periods").newColumn("info").isJson(true).jsonKey("is_same_periods").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("attribute2").newColumn("info").isJson(true).jsonKey("hope_the_meeting_date").newColumnJavaType("datetime").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("attribute1").newColumn("info").isJson(true).jsonKey("meeting_type").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_meeting_apply_check").newTableName("rms_project_check").oldColumn("is_collect_financial_info").newColumn("info").isJson(true).jsonKey("is_collect_financial_info").newColumnJavaType("char").newDbDefault("null").build());

//         rms_project_module
//        stop
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("stop_explanation").newColumn("correct_explanation").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("is_announce").newColumn("is_announce").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("attribute5").newColumn("father_project_phase").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("stop_date").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("stop_type").newColumn("correct_type").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("rms_project_module").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char").newDbDefault("null").build());
//        re_rating
//    id                         char(19)      not null
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("is_valid").newColumn("is_valid").newColumnJavaType("tinyint(1)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("number_of_reviews").newColumn("number_of_reviews").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("re_rating_reason").newColumn("re_rating_reason").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("re_rating_sponsor").newColumn("re_rating_sponsor").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("re_rating_time").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("rms_project_module").oldColumn("further_information").newColumn("further_information").newColumnJavaType("varchar(2000)").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("re_rating_time").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("track_state").newColumn("track_state").newColumnJavaType("char(255)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("rack_year").newColumn("rack_year").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("is_hanging").newColumn("is_hanging").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("attribute5").newColumn("last_re_rating_time").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("track_dif").newColumn("track_dif").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("rms_project_module").oldColumn("track_fee").newColumn("track_fee").newColumnJavaType("decimal(19 2)").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("update_explanation").newColumn("update_explanation").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("update_sponsor").newColumn("update_sponsor").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("attribute4").newColumn("is_valid").newColumnJavaType("tinyint(1)").newDbDefault("1").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("attribute3").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("rms_project_module").oldColumn("attribute5").newColumn("update_count").newColumnJavaType("int").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("attribute5").newColumn("other_stages").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("meeting_record_id").newColumn("meeting_record_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("main_level").newColumn("main_level").newColumnJavaType("varchar(1000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("judge_name").newColumn("judge_ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("main_name").newColumn("main_ccxi_inst_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("is_main").newColumn("is_main").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("main_id").newColumn("main_ccxi_inst_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("outlook_level").newColumn("outlook_level").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("judge_id").newColumn("judge_ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("attribute4").newColumn("debt_outlook").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("attribute5").newColumn("other_conclusions").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("meeting_record_id").newColumn("meeting_record_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("debt_level").newColumn("debt_level").newColumnJavaType("varchar(1000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("judge_name").newColumn("judge_ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("judge_id").newColumn("judge_ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("attribute5").newColumn("vote_info").newColumnJavaType("json").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("attribute1").newColumn("judge_type").newColumnJavaType("varchar(31)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("attribute4").newColumn("type").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("meeting_record_id").newColumn("meeting_record_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("vote_level").newColumn("vote_level").newColumnJavaType("varchar(3000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("option_dif").newColumn("option_dif").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("order_no").newColumn("order_no").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("id").newColumn("id").newColumnJavaType("char(19)").newDbDefault("not").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("attribute1").newColumn("is_make_notice").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("attribute3").newColumn("make_notice_log").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("attribute5").newColumn("other_report_name").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("batch_project_code").newColumn("project_code").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("batch_project_name").newColumn("project_name").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("project_en_name").newColumn("project_en_name").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("project_en_name").newColumn("project_en_name").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("debt_cn_short").newColumn("debt_cn_short").newColumnJavaType("varchar(2000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("year_report_year").newColumn("year_report_year").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("proposal_level").newColumn("proposal_level").newColumnJavaType("varchar(3000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("quantitaive_model").newColumn("quantitaive_model").newColumnJavaType("varchar(600)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("main_id").newColumn("main_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("user_id").newColumn("ccxi_user_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("user_name").newColumn("ccxi_user_name").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("report_name").newColumn("report_name").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("debt_level").newColumn("debt_level").newColumnJavaType("varchar(700)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("last_debt_level").newColumn("last_debt_level").newColumnJavaType("varchar(700)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("issuing_amount").newColumn("issuing_amount").newColumnJavaType("varchar(800)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("dept_id").newColumn("dept_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("dept_name").newColumn("dept_name").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("scoring_industry").newColumn("scoring_industry").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("is_hanging").newColumn("is_hanging").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("batch_id").newColumn("batch_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("launch_explain").newColumn("launch_explain").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("project_members").newColumn("project_members").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("report_members").newColumn("report_members").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("trig_incident").newColumn("trig_incident").newColumnJavaType("varchar(1500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("is_negative").newColumn("is_negative").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("record_file_check").newColumn("record_file_check").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("record_file_code").newColumn("record_file_code").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("record_file_memberid").newColumn("record_file_memberid").newColumnJavaType("varchar(25)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("record_file_member").newColumn("record_file_member").newColumnJavaType("varchar(25)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("is_interview").newColumn("is_interview").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("other_industry_reason").newColumn("other_industry_reason").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("file_system_id").newColumn("file_system_id").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("issue_market").newColumn("issue_market").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("hang_type").newColumn("hang_type").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("over_operate_user").newColumn("over_operate_user").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("attribute4").newColumn("report_type").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("sub_module").newColumn("sub_module").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("operate_over").newColumn("operate_over").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("operate_end").newColumn("operate_end").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("launch_date").newColumn("launch_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("trig_date").newColumn("trig_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("second_aryarchive_date").newColumn("second_aryarchive_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("feedback_inspection_date").newColumn("feedback_inspection_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("rectify_completed_date").newColumn("rectify_completed_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("re_allot").newColumn("re_allot").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("before_corp_tag").newColumn("before_corp_tag").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("over_time").newColumn("over_time").newColumnJavaType("datetime").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("is_main_upgrade").newColumn("is_main_upgrade").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("operate_hang_time").newColumn("operate_hang_time").newColumnJavaType("datetime").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project").newTableName("rms_project").oldColumn("attribute2").newColumn("notice_date").newColumnJavaType("date").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("id").newColumn("id").newColumnJavaType("char(19)").newDbDefault("not").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("attribute2").newColumn("other_report_name").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("attribute3").newColumn("report_name").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("attribute4").newColumn("is_transfer_level").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("attribute5").newColumn("scoring_industry").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("project_id").newColumn("project_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("project_code").newColumn("project_code").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("project_name").newColumn("project_name").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("project_en_name").newColumn("project_en_name").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("sub_module_id").newColumn("sub_module_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("father_id").newColumn("father_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_explanation").newColumn("operate_explanation").newColumnJavaType("varchar(4000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("year_report_year").newColumn("year_report_year").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("proposal_level").newColumn("proposal_level").newColumnJavaType("varchar(3000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("quantitaive_model").newColumn("quantitaive_model").newColumnJavaType("varchar(600)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("account_code").newColumn("account_code").newColumnJavaType("char(13)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("debt_level").newColumn("debt_level").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("last_debt_level").newColumn("last_debt_level").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("issuing_amount").newColumn("issuing_amount").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("project_members").newColumn("project_members").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("report_members").newColumn("report_members").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("level_remark").newColumn("level_remark").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("trig_incident").newColumn("trig_incident").newColumnJavaType("varchar(3000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("is_negative").newColumn("is_negative").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_projectname").newColumn("operate_projectname").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_members").newColumn("operate_members").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("record_file_check").newColumn("record_file_check").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("record_file_code").newColumn("record_file_code").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("record_file_memberid").newColumn("record_file_memberid").newColumnJavaType("varchar(25)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("record_file_member").newColumn("record_file_member").newColumnJavaType("varchar(25)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("other_industry_reason").newColumn("other_industry_reason").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("is_interview").newColumn("is_interview").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("file_system_id").newColumn("file_system_id").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("issue_market").newColumn("issue_market").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("xshy_project_id").newColumn("xshy_project_id").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("year_report_bak").newColumn("year_report_bak").newColumnJavaType("varchar(30)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("over_operate_user").newColumn("over_operate_user").newColumnJavaType("varchar(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("trig_date").newColumn("trig_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("hang_type").newColumn("hang_type").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("is_main_upgrade").newColumn("is_main_upgrade").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("feedback_inspection_date").newColumn("feedback_inspection_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_hang_time").newColumn("operate_hang_time").newColumnJavaType("datetime").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("rectify_completed_date").newColumn("rectify_completed_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("attribute1").newColumn("report_type").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("second_aryarchive_date").newColumn("second_aryarchive_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("sub_module").newColumn("sub_module").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_over").newColumn("operate_over").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_end").newColumn("operate_end").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("notice_date").newColumn("notice_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("make_notice_flg").newColumn("make_notice_flg").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("operate_log").newColumn("operate_log").newColumnJavaType("text").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project").oldColumn("over_time").newColumn("over_time").newColumnJavaType("datetime").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("id").oldColumn("id").newDbDefault("not").newColumnJavaType("bigint").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("usable_status").oldColumn("usable_status").newDbDefault("null").newColumnJavaType("tinyint").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("rms_project_id").oldColumn("batch_project_id").newDbDefault("null").newColumnJavaType("bigint").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("project_register_id").oldColumn("project_id").newDbDefault("null").newColumnJavaType("bigint").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("operate_id").oldColumn("operate_id").newDbDefault("null").newColumnJavaType("bigint").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("sub_module").oldColumn("sub_module").newDbDefault("null").newColumnJavaType("int").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("debt_level").oldColumn("attribute2").newDbDefault("null").newColumnJavaType("varchar(900)").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_project_list").newTableName("rms_project_relation").newColumn("debt").oldColumn("attribute1").newDbDefault("null").newColumnJavaType("varchar(900)").build());

        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project_relation").newColumn("id").oldColumn("id").newColumnJavaType("bigint").newDbDefault("not").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project_relation").newColumn("usable_status").oldColumn("usable_status").newColumnJavaType("tinyint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project_relation").newColumn("sub_module").oldColumn("sub_module").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project_relation").newColumn("project_register_id").oldColumn("project_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project_relation").newColumn("operate_id").oldColumn("operate_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_operate").newTableName("rms_project_relation").newColumn("debt_level").oldColumn("debt_level").newColumnJavaType("varchar(900)").newDbDefault("null").build());
    }}.stream()
            .peek(singletonMapRule -> singletonMapRule
                    .oldMainIdName("id")
                    .newMainIdName("id")
                    .newDbDefault(singletonMapRule.newDbDefault().equalsIgnoreCase("not") ? "not null" : singletonMapRule.newDbDefault())
                    .tableType(2))
            .collect(Collectors.toList());
}
