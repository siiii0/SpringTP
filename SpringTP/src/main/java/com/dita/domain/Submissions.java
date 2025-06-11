package com.dita.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submissions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submissions {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int s_id;
	
	@ManyToOne
	   @JoinColumns({
	       @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
	       @JoinColumn(name = "user_type", referencedColumnName = "user_type")
	   })
	private User user;
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "q_id", referencedColumnName = "q_id"),
    })
	private Question qid;
	
	@Column(columnDefinition = "varchar(20)", nullable = false)
	private String s_language;
	
	@Column(columnDefinition = "text", nullable = false)
	private String s_code;
	
	@Column(columnDefinition = "varchar(1)", nullable = true)
	private String s_isCorrect;
	
	@Column(nullable = true)
	private int s_runTime;
	
	@Column(nullable = false)
	private LocalDateTime submitted_at;
}
