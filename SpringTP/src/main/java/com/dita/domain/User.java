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
    private User_id_type idType; // 복합키 - 필드명 변경 (IdType -> idType)
    
    @Column(name ="user_pwd", columnDefinition = "varchar(100)", nullable = true)
	private String userPwd;
    
    @Column(name="user_profile",columnDefinition = "text", nullable = true)
	private String userProfile;
    
    @Column(name="user_email",columnDefinition = "varchar(30)", nullable = true)
	private String userEmail;
    
    @Column(name="user_score",nullable = true)
	private int userScore = 0;
    
    @Column(name="user_grade",columnDefinition = "varchar(10) default '일반'", nullable = true)
	private String userGrade;
    
    @Column(name="user_school",columnDefinition = "varchar(30)", nullable = true)
	private String userSchool;
    
    @Column(name="registered_at",nullable = true)
	private LocalDateTime registeredAt;
	
	@Column(name="user_wd",columnDefinition = "varchar(1) default 'N'", nullable = true)
	private String userWd;
	
	@Column(name="user_wd_reason",columnDefinition = "text", nullable = true)
	private String userWdReason;
	
	@Column(name="user_wd_date",nullable = true)
	private LocalDateTime userWdDate;
}
