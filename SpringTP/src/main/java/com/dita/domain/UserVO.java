package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	private String user_id;
	private String user_type;
	private String user_pwd;
	private String user_profile;
	private String user_email;
	private int user_score;
	private String user_grade;
	private String user_school;
	private String registered_at;
	private String user_wd;
	private String user_wd_reason;
	private String user_wd_date;
}
