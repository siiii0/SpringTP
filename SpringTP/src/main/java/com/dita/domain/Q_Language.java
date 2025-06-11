package com.dita.domain;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
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
	private int ql_id;
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "q_id", referencedColumnName = "q_id"),
    })
	private Question qId;
	
	@Column(columnDefinition = "varchar(20)", nullable = false, name = "q_language")
	private String QLanguage;
	
	@Column(columnDefinition = "text", nullable = false)
	private String q_answer;
}
