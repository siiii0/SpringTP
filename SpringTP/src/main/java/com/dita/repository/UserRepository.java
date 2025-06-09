package com.dita.repository;

import com.dita.domain.User;
import com.dita.domain.User_id_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}

