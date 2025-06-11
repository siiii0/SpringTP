package com.dita.repository;

import com.dita.domain.Question;
import com.dita.domain.Submissions;
import com.dita.domain.User;

import com.dita.domain.User_id_type;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Integer> {

    // 특정 사용자가 맞은 제출 내역 조회 - User 객체 직접 사용
    @Query("SELECT s FROM Submissions s WHERE s.user = :user AND s.s_isCorrect = 'Y'")
    List<Submissions> findCorrectSubmissionsByUser(@Param("user") User user);
    
    // 특정 사용자가 맞은 제출 내역 조회 - 복합키 사용 (대체 메소드)
    @Query("SELECT s FROM Submissions s WHERE s.user.idType.userId = :userId AND s.user.idType.userType = :userType AND s.s_isCorrect = 'Y'")
    List<Submissions> findCorrectSubmissionsByUserIdAndType(@Param("userId") String userId, @Param("userType") String userType);
    
    // 특정 사용자가 맞은 고유한 문제 목록 조회 - User 객체 직접 사용
    @Query("SELECT DISTINCT s.q_id FROM Submissions s WHERE s.user = :user AND s.s_isCorrect = 'Y'")
    List<Question> findDistinctCorrectQuestionsByUser(@Param("user") User user);
    
    // 특정 사용자가 맞은 고유한 문제 목록 조회 - 복합키 사용 (대체 메소드)
    @Query("SELECT DISTINCT s.q_id FROM Submissions s WHERE s.user.idType.userId = :userId AND s.user.idType.userType = :userType AND s.s_isCorrect = 'Y'")
    List<Question> findDistinctCorrectQuestionsByUserIdAndType(@Param("userId") String userId, @Param("userType") String userType);
    
    // 특정 사용자가 맞은 고유한 문제 수 카운트
    @Query("SELECT COUNT(DISTINCT s.q_id) FROM Submissions s WHERE s.user.idType.userId = :userId AND s.user.idType.userType = :userType AND s.s_isCorrect = 'Y'")
    long countDistinctCorrectQuestionsByUserIdAndType(@Param("userId") String userId, @Param("userType") String userType);
    
    // 특정 학교/회사에 속한 사용자들이 맞은 고유한 문제 수 조회
    @Query("SELECT COUNT(DISTINCT s.q_id) FROM Submissions s JOIN s.user u WHERE u.userSchool = :orgName AND s.s_isCorrect = 'Y'")
    long countDistinctCorrectQuestionsByOrg(@Param("orgName") String orgName);
    
    // 특정 학교/회사에 속한 사용자들의 총 제출 수
    @Query("SELECT COUNT(s) FROM Submissions s JOIN s.user u WHERE u.userSchool = :orgName")
    long countSubmissionsByOrg(@Param("orgName") String orgName);
    
    // 특정 학교/회사에 속한 사용자들의 맞은 제출 수
    @Query("SELECT COUNT(s) FROM Submissions s JOIN s.user u WHERE u.userSchool = :orgName AND s.s_isCorrect = 'Y'")
    long countCorrectSubmissionsByOrg(@Param("orgName") String orgName);
    
    // 특정 사용자의 총 제출 수
    @Query("SELECT COUNT(s) FROM Submissions s WHERE s.user.idType.userId = :userId AND s.user.idType.userType = :userType")
    long countSubmissionsByUserIdAndType(@Param("userId") String userId, @Param("userType") String userType);
    
    // 특정 사용자의 맞은 제출 수
    @Query("SELECT COUNT(s) FROM Submissions s WHERE s.user.idType.userId = :userId AND s.user.idType.userType = :userType AND s.s_isCorrect = 'Y'")
    long countCorrectSubmissionsByUserIdAndType(@Param("userId") String userId, @Param("userType") String userType);

    // User 엔티티와 qId로 Submissions을 조회하는 메서드
    List<Submissions> findByqIdAndUser(Question qId, User user); 

}
 