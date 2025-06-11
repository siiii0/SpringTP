package com.dita.repository;

import com.dita.domain.User;
import com.dita.domain.User_id_type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, User_id_type> {
	
	// 복합키를 기준으로 User 조회
    Optional<User> findById(User_id_type userIdType); // 복합키로 사용자 조회

    // JPQL을 사용하여 명시적으로 쿼리 작성
    @Query("SELECT u FROM User u WHERE u.idType.userId = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);
    
    // 복합키의 두 필드로 조회
    @Query("SELECT u FROM User u WHERE u.idType.userId = :userId AND u.idType.userType = :userType")
    Optional<User> findByUserIdAndUserType(@Param("userId") String userId, @Param("userType") String userType);
    
    // 아이디 중복 체크 - 수정된 쿼리
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.idType.userId = :userId")
    boolean existsByUserId(@Param("userId") String userId);
    
    // 개인 랭킹 - 총 점수 높은 순으로 페이징 처리
    @Query("SELECT u FROM User u WHERE u.userWd = 'N' ORDER BY u.userScore DESC")
    Page<User> findAllUsersByScoreDesc(Pageable pageable);
    
    // 특정 등급의 사용자 목록 - 총 점수 높은 순 (등급이 null인 경우 '일반'으로 처리)
    @Query("SELECT u FROM User u WHERE u.userWd = 'N' AND (u.userGrade = :grade OR (:grade = '일반' AND (u.userGrade IS NULL OR u.userGrade = '일반'))) ORDER BY u.userScore DESC")
    Page<User> findUsersByGrade(@Param("grade") String grade, Pageable pageable);
    
    // 소속별 랭킹을 위한 조회 - 같은 학교/회사별로 그룹화하고 점수 합산
    @Query("SELECT u.userSchool, COUNT(u), SUM(u.userScore) FROM User u WHERE u.userWd = 'N' AND u.userSchool IS NOT NULL GROUP BY u.userSchool ORDER BY SUM(u.userScore) DESC")
    Page<Object[]> findUsersByOrganizationRanking(Pageable pageable);
    
    // 디버깅: 모든 사용자의 등급 출력
    @Query("SELECT u.idType.userId, u.userGrade FROM User u WHERE u.userWd = 'N'")
    List<Object[]> findAllUsersWithGrades();
    
    // 이메일로 사용자 아이디 찾기
    @Query("SELECT u.idType.userId FROM User u WHERE u.userEmail = :email AND u.userWd = 'N'")
    Optional<String> findUserIdByEmail(@Param("email") String email);
}

