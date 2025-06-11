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
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int b_id;
    
    @Column(columnDefinition = "varchar(20)", nullable = false, name = "b_type")
    private String bType;
    
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String b_title;
    
    @Column(columnDefinition = "text", nullable = false)
    private String b_content;
    
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumns({
	 * 
	 * @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
	 * 
	 * @JoinColumn(name = "user_type", referencedColumnName = "user_type") })
	 * private User user;
	 */
    @Column(columnDefinition = "varchar(30)", nullable = true)
    private String user_id;
    
    @Column(columnDefinition = "varchar(10)", nullable = true)
    private String user_type;
    
    @Column(nullable = false)
    private LocalDateTime created_at;
    
    @Column(nullable = true)
    private Integer b_comments = 0;
    
    @Column(nullable = true)
    private Integer b_views = 0;
    
    @Column(nullable = true)
    private Integer b_likes = 0;
    
    @Column(columnDefinition = "varchar(5) default 'N'", nullable = true, name = "b_isPinned")
    private String BIsPinned;
}
