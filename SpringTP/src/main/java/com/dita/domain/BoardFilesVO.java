package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardFilesVO {
	private int bf_id;
	private int b_id;
	private String bf_files;
}
