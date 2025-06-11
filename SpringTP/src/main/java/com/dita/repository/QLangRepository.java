package com.dita.repository;

import com.dita.domain.Q_Language;
import com.dita.domain.Question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QLangRepository extends JpaRepository<Q_Language, Integer> {
	List<Q_Language> findByqId(Question question);
	
    // q_id와 q_language를 기준으로 단일 Q_Language 객체 반환
    Optional<Q_Language> findByqIdAndQLanguage(Question question, String qLanguage);

//    // Question 객체로 조회
//    Q_Language findByqId(Question question); // `Question` 객체를 전달

}
