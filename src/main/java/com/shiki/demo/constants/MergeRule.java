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
//        business_check
//        one
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("attribute5").newColumn("project_phase").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("is_batch").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_one_check").newTableName("business_check").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());
//        two
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("attribute5").newColumn("project_phase").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("is_batch").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_two_check").newTableName("business_check").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());
//        zpthree
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("attribute5").newColumn("project_phase").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("is_meet").newColumn("is_meet").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("is_zpthree_check").newColumn("is_zpthree_check").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("is_batch").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("no_meeting_reason").newColumn("no_meeting_reason").newColumnJavaType("varchar(1000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_zpthree_check").newTableName("business_check").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());
//        three
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint(20)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("attribute5").newColumn("project_phase").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("check_opinion").newColumn("check_opinion").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("check_date").newColumn("check_date").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("is_batch").newColumn("is_batch").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("is_new").newColumn("is_new").newColumnJavaType("varchar(10)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_three_check").newTableName("business_check").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());
//         business_project_phase
//        stop
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("stop_explanation").newColumn("correct_explanation").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("is_announce").newColumn("is_announce").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("attribute5").newColumn("father_project_phase").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("stop_date").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("stop_type").newColumn("correct_type").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("check_state").newColumn("check_state").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_stop").newTableName("business_project_phase").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());
//        re_rating
//    id                         char(19)      not null
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("is_valid").newColumn("is_valid").newColumnJavaType("tinyint(1)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("number_of_reviews").newColumn("number_of_reviews").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("re_rating_reason").newColumn("re_rating_reason").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("re_rating_sponsor").newColumn("re_rating_sponsor").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("re_rating_time").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_re_rating").newTableName("business_project_phase").oldColumn("further_information").newColumn("further_information").newColumnJavaType("varchar(2000)").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("re_rating_time").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("track_state").newColumn("track_state").newColumnJavaType("char(255)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("rack_year").newColumn("rack_year").newColumnJavaType("varchar(50)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("is_hanging").newColumn("is_hanging").newColumnJavaType("tinyint(1)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("attribute5").newColumn("last_re_rating_time").newColumnJavaType("date").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("track_dif").newColumn("track_dif").newColumnJavaType("char").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("track_fee").newColumn("track_fee").newColumnJavaType("decimal(19 2)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_track").newTableName("business_project_phase").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("attribute1").newColumn("ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("attribute2").newColumn("ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("operate_id").newColumn("operate_id").newColumnJavaType("char(19)").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("usable_status").newColumn("usable_status").newColumnJavaType("varchar(3)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("update_explanation").newColumn("update_explanation").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("update_sponsor").newColumn("update_sponsor").newColumnJavaType("varchar(100)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("attribute4").newColumn("is_valid").newColumnJavaType("tinyint(1)").newDbDefault("1").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("attribute3").newColumn("extend_time").newColumnJavaType("date").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("attribute5").newColumn("update_count").newColumnJavaType("int").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_project_update").newTableName("business_project_phase").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());

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
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_result").newTableName("business_judge_vote_result").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("attribute4").newColumn("debt_outlook").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("attribute5").newColumn("other_conclusions").newColumnJavaType("varchar(500)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("meeting_record_id").newColumn("meeting_record_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("debt_level").newColumn("debt_level").newColumnJavaType("varchar(1000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("judge_name").newColumn("judge_ccxi_user_name").newColumnJavaType("varchar(200)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("judge_id").newColumn("judge_ccxi_user_id").newColumnJavaType("bigint").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_judges_vote_debt_result").newTableName("business_judge_vote_result").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());

        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("id").newColumn("id").newColumnJavaType("bigint").newDbDefault("not null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("attribute5").newColumn("vote_info").newColumnJavaType("json").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("attribute1").newColumn("judge_type").newColumnJavaType("varchar(31)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("attribute4").newColumn("type").newColumnJavaType("varchar(900)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("meeting_record_id").newColumn("meeting_record_id").newColumnJavaType("char(19)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("vote_level").newColumn("vote_level").newColumnJavaType("varchar(3000)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("option_dif").newColumn("option_dif").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("order_no").newColumn("order_no").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("remark").newColumn("remark").newColumnJavaType("varchar(300)").newDbDefault("null").build());
        add(SingletonMapRule.builder().oldTableName("business_batch_level_result").newTableName("business_judge_vote_result").oldColumn("history").newColumn("history").newColumnJavaType("json").newDbDefault("null").build());

        }}.stream().map(singletonMapRule -> singletonMapRule.oldMainIdName("id").newMainIdName("id").tableType(2)).collect(Collectors.toList());
}
