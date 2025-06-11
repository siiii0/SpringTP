package com.dita.repository;

import com.dita.domain.Board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
	List<Board> findBybTypeAndBIsPinned(String b_type, String b_isPinned);
	List<Board> findBybType(String b_type);
	Board findById(int b_id);
}
 