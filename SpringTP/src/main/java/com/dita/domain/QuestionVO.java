package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVO {
	private int q_id;
	private String q_title;
	private String q_content;
	private String q_difficulty;
	private int q_crt_cnt;
	private int q_submits;
	private double q_crt_per;
	private int q_recommend;
	private String user_id;
	private String user_type;
	private String created_at;
}
