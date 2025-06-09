package com.dita.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dita.domain.User;
import com.dita.domain.User_id_type;

@Repository
public interface UserRepository extends JpaRepository<User, User_id_type>{
    // 복합키를 기준으로 User 조회
    Optional<User> findById(User_id_type userIdType); // 복합키로 사용자 조회
	
}