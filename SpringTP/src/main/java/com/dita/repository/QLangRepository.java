package com.dita.repository;

import com.dita.domain.Q_Language;
import com.dita.domain.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QLangRepository extends JpaRepository<Q_Language, Integer> {
	List<Q_Language> findByqId(Question question);

}
