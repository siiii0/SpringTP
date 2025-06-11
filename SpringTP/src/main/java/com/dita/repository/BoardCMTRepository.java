package com.dita.repository;

import com.dita.domain.Board;
import com.dita.domain.BoardCmt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCMTRepository extends JpaRepository<BoardCmt, Integer> {
	List<BoardCmt> findBybId(Board b_id);
}
 