package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionsVO {
	private String user_id;
	private String user_type;
	private int q_id;
	private String s_language;
	private String s_code;
	private String s_isCorrect;
	private int s_runTime;
	private String submitted_at;
}
