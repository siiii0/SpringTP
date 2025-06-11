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
@Table(name = "boardcmt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCmt {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bc_id;
    
	@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "b_id", referencedColumnName = "b_id"),
    })
	private Board bId;
	
	@Column(columnDefinition = "text", nullable = false)
	private String bc_content;
	
	@Column(nullable = false)
	private LocalDateTime answered_at;
	
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
	
	@Column(nullable = true)
	private int bc_likes = 0;
	
	@Column(nullable = true)
	private int bc_ref;
	
	@Column(nullable = true)
	private int bc_comment = 0;
}
