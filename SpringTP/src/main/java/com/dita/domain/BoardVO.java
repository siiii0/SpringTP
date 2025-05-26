package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardVO {
	private int b_id;
	private String b_type;
	private String b_title;
	private String b_content;
	private String user_id;
	private String user_type;
	private String created_at;
	private int b_comments;
	private int b_views;
	private int b_likes;
	private String b_isPinned;
}
