package com.dita.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "q_language")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Q_Language {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int q_id;
	
	@Column(columnDefinition = "varchar(20)", nullable = false)
	private String q_language;
	
	@Column(columnDefinition = "text", nullable = false)
	private String q_answer;
}
