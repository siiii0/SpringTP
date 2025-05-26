package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCmtVO {
	private int bc_id;
	private int b_id;
	private String bc_content;
	private String answered_at;
	private String user_id;
	private String user_type;
	private int bc_likes;
	private int bc_ref;
	private int bc_comment;
}
