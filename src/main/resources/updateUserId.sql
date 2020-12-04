alter table rm_user  add COLUMN ccxi_user_id BIGINT null AFTER id;

alter table rm_party_role  add COLUMN ccxi_user_id BIGINT null AFTER owner_party_id;

alter table rm_party  add COLUMN ccxi_user_id BIGINT null AFTER id;


alter table business_batch_level_result  add COLUMN ccxi_user_id BIGINT null AFTER attribute5;

alter table business_check_opinion_record  add COLUMN ccxi_user_id BIGINT null AFTER attribute2 ;

alter table business_check_opinion_record  add COLUMN check_ccxi_user_id BIGINT null after check_user_id ;

alter table business_meeting_analyst_history_record  add COLUMN ccxi_user_id BIGINT null AFTER attribute2;

alter table business_meeting_analyst_record  add COLUMN ccxi_user_id BIGINT null AFTER attribute2;

alter table business_one_check  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_project_re_rating  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_project_stop  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_project_track  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_project_update  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_three_check  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_two_check  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table business_zpthree_check  add COLUMN ccxi_user_id BIGINT null AFTER attribute1;

alter table base_ccxi_judges_info  add COLUMN judges_ccxi_user_id BIGINT null AFTER judges_id;

ALTER TABLE base_associated_document  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_ccxi_judges_info  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_contact_info  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_document_name_definition  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_document_relationship_maintenance  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_enterprise_agency_change_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_enterprise_agency_info  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_impulse_sender  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_main_level_change_detail  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE base_opinion_list  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_batch_judges_vote  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_batch_level_result  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_batch_project  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_batch_project_list  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_batch_project_meeting_result  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_bond_issue  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_check_opinion_big_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_check_opinion_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_compliance_archive  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_compliance_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_enterprise_agency_info  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_file_version_history  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_interview_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_interview_record_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_judges_vote_debt_result  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_judges_vote_result  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_analyst_history_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_analyst_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_analyst_record_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_apply_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_list  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_main_level  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_participants  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_project  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_record  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_meeting_secretary_record_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_notice_review  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_one_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_operate  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_archive  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_archive_list  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_file  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_hanging  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_notice  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_operate_end  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_register  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_re_rating  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_staffing  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_stop  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_track  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_project_update  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_promise_secrecy  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_three_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_transfer_path  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_two_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_zpthree_check  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE crm_address_head  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE crm_area  add COLUMN create_ccxi_user_id bigint null after create_user_id;

ALTER TABLE business_batch_judges_vote  add COLUMN judge_ccxi_user_id bigint null after judge_id;

ALTER TABLE business_judges_vote_debt_result  add COLUMN judge_ccxi_user_id bigint null after judge_id;

ALTER TABLE business_judges_vote_result  add COLUMN judge_ccxi_user_id bigint null after judge_id;

ALTER TABLE business_batch_project  add COLUMN ccxi_user_id bigint null after user_id;

ALTER TABLE business_batch_project  add COLUMN record_file_member_ccxi_user_id bigint null after record_file_memberid;

ALTER TABLE business_operate  add COLUMN record_file_member_ccxi_user_id bigint null after record_file_memberid;

ALTER TABLE business_interview_record_check  add COLUMN recorder_ccxi_user_id bigint null after recorder_id;

ALTER TABLE business_meeting_analyst_history_record  add COLUMN recorder_ccxi_user_id bigint null after recorder_id;

ALTER TABLE business_meeting_analyst_record  add COLUMN recorder_ccxi_user_id bigint null after recorder_id;

ALTER TABLE business_meeting_analyst_record_check  add COLUMN recorder_ccxi_user_id bigint null after recorder_id;

ALTER TABLE business_meeting_secretary_record_check  add COLUMN recorder_ccxi_user_id bigint null after recorder_id;

ALTER TABLE business_interview_record_check  add COLUMN checker_ccxi_user_id bigint null after checker_id;

ALTER TABLE business_meeting_analyst_history_record  add COLUMN checker_ccxi_user_id bigint null after checker_id;

ALTER TABLE business_meeting_analyst_record  add COLUMN checker_ccxi_user_id bigint null after checker_id;

ALTER TABLE business_meeting_analyst_record_check  add COLUMN checker_ccxi_user_id bigint null after checker_id;

ALTER TABLE business_meeting_secretary_record_check  add COLUMN checker_ccxi_user_id bigint null after checker_id;

ALTER TABLE business_notice_review  add COLUMN checker_ccxi_user_id bigint null after checker_id;

ALTER TABLE business_meeting_apply_check  add COLUMN proposer_ccxi_user_id bigint null after proposer_id;

ALTER TABLE business_project_operate_end  add COLUMN proposer_ccxi_user_id bigint null after proposer_id;

ALTER TABLE business_meeting_apply_check  add COLUMN auditor_ccxi_user_id bigint null after auditor_id;

ALTER TABLE business_project_operate_end  add COLUMN auditor_ccxi_user_id bigint null after auditor_id;

ALTER TABLE business_meeting_list  add COLUMN committee_ccxi_user_id bigint null after committee_id;

ALTER TABLE business_meeting_participants  add COLUMN committee_ccxi_user_id bigint null after committee_id;

ALTER TABLE business_project_staffing  add COLUMN project_member_ccxi_user_id bigint null after project_member_id;

ALTER TABLE business_promise_secrecy  add COLUMN seal_ccxi_user_id bigint null after seal_user_id;

ALTER TABLE crm_address_head  add COLUMN head_ccxi_user_id bigint null after head_id;

ALTER TABLE rm_party_relation  add COLUMN child_party_ccxi_user_id bigint null after child_party_id;
