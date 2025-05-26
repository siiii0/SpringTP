package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVO {
	private int n_id;
	private String n_type;
	private String n_title;
	private String n_content;
	private String n_url;
	private String user_id;
	private String user_type;
	private String n_isRead;
	private String created_at;
}
