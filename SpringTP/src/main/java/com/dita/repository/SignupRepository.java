package com.dita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dita.domain.User;
import com.dita.domain.User_id_type;

@Repository
public interface SignupRepository extends JpaRepository<User, User_id_type>{
	
	boolean existsById(User_id_type user_id);
}
/*
 * package com.dita.repository;
 * 
 * import org.springframework.data.jpa.repository.JpaRepository;
 * 
 * import com.dita.domain.User;
 * 
 * public interface SignupRepository extends JpaRepository<User, Long>{
 * 
 * }
 */
