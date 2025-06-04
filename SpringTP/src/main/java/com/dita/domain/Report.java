package com.dita.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int r_id;
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "b_id", referencedColumnName = "b_id"),
    })
	private Board b_id;
	
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "bc_id", referencedColumnName = "bc_id"),
    })
	private BoardCmt bc_id;
	
	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String r_content;
	
	@Column(columnDefinition = "text", nullable = true)
	private String r_detail;
	
	@ManyToOne
	   @JoinColumns({
	       @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
	       @JoinColumn(name = "user_type", referencedColumnName = "user_type")
	   })
	private User user;
	
	@Column(nullable = false)
	private LocalDateTime reported_at;
	
	@Column(columnDefinition = "varchar(5) default 'N'", nullable = true)
	private String r_isProc;
	
	@Column(nullable = true)
	private LocalDateTime processed_at;
}
