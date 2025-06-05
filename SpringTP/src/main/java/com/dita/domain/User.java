package com.dita.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
    @EmbeddedId
    private User_id_type id_type; // 복합키
    
    @Column(columnDefinition = "varchar(20)", nullable = true)
	private String user_pwd;
    
    @Column(columnDefinition = "text", nullable = true)
	private String user_profile;
    
    @Column(columnDefinition = "varchar(30)", nullable = true)
	private String user_email;
    
    @Column(nullable = true)
	private int user_score = 0;
    
    @Column(columnDefinition = "varchar(10) default '일반'", nullable = true)
	private String user_grade;
    
    @Column(columnDefinition = "varchar(30)", nullable = true)
	private String user_school;
    
    @Column(nullable = true)
	private LocalDateTime registered_at;
	
	@Column(columnDefinition = "varchar(1) default 'N'", nullable = true)
	private String user_wd;
	
	@Column(columnDefinition = "text", nullable = true)
	private String user_wd_reason;
	
	@Column(nullable = true)
	private LocalDateTime user_wd_date;
}
