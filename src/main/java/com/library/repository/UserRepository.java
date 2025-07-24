package com.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	List<User> findByUserNameContaining(String userName);
	
	@Query("SELECT u FROM User u WHERE u.userName = :username")
	Optional<User> findUserName(@Param("username") String username);
	
	List<User> findByStatus(String status);

	@Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
	List<User> findActiveUsers();

	@Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
	long countActiveUsers();
	
	@Query("SELECT u.role FROM User u WHERE u.userName = :username")
	String getRole(@Param("username") String username);
}
