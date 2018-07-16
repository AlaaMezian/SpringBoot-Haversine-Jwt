package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);
	
	List<User> findAllByActive(boolean status);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	User findByActiveAndId(boolean status, long id);
}