package com.dita.domain;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boardfiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardFiles {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bf_id;
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "b_id", referencedColumnName = "b_id"),
    })
	private Board b_id;
	
	@Column(columnDefinition = "text", nullable = false)
	private String bf_files;
}
