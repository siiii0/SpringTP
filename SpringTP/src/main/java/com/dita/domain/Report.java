package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
	private int r_id;
	private int b_id;
	private int bc_id;
	private String r_content;
	private String r_detail;
	private String user_id;
	private String user_type;
	private String reported_at;
	private String r_isProc;
	private String processed_at;
}
