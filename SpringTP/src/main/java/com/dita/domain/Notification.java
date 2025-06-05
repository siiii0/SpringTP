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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int n_id;
	
	@Column(columnDefinition = "varchar(10)", nullable = true)
	private String n_type;
	
	@Column(columnDefinition = "varchar(50)", nullable = false)
	private String n_title;
	
	@Column(columnDefinition = "text", nullable = false)
	private String n_content;
	
	@Column(columnDefinition = "text", nullable = true)
	private String n_url;
	
	@ManyToOne
	   @JoinColumns({
	       @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
	       @JoinColumn(name = "user_type", referencedColumnName = "user_type")
	   })
	private User user;
	
	@Column(columnDefinition = "varchar(5) default 'N'", nullable = true)
	private String n_isRead;
	
	@Column(nullable = false)
	private LocalDateTime created_at;
}
