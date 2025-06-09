package com.dita.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "q_id")
	private int qId;
	
	@Column(unique = true, nullable = true)
	private int q_num;
	
	@Column(columnDefinition = "text", nullable = false)
	private String q_title;
	
	@Column(columnDefinition = "text", nullable = false)
	private String q_content;
	
	@Column(columnDefinition = "varchar(10)", nullable = true)
	private String q_difficulty;
	
	@Column(nullable = true)
	private int q_crt_cnt = 0;
	
	@Column(nullable = true)
	private int q_submits = 0;
	
	@Column(nullable = true)
	private double q_crt_per = 0;
	
	@Column(nullable = true)
	private int q_recommend = 0;
	
	@Column(columnDefinition = "text", nullable = true)
	private String q_input;
	
	@Column(columnDefinition = "text", nullable = true)
	private String q_output;
	
	@Column(columnDefinition = "text", nullable = true, name = "q_input_ex")
	private String q_inputEx;
	
	@Column(columnDefinition = "text", nullable = true, name = "q_output_ex")
	private String q_outputEx;
	
	
	@ManyToOne
	   @JoinColumns({
	       @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
	       @JoinColumn(name = "user_type", referencedColumnName = "user_type")
	   })
	private User user;
	
	@Column(nullable = false)
	private LocalDateTime created_at;
}
