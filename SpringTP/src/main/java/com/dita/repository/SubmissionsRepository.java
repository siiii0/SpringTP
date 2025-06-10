package com.dita.repository;

import com.dita.domain.Question;
import com.dita.domain.Submissions;
import com.dita.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Integer> {
    // User 엔티티와 qId로 Submissions을 조회하는 메서드
    List<Submissions> findByqIdAndUser(Question qId, User user); 

}
 