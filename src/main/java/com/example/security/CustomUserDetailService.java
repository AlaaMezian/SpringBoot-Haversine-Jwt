package com.example.security;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public AuthenticatedUser loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Let people login with either username or email
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + usernameOrEmail));

		if (user == null) {
			throw new BadRequestException("the user cant be authenticated");
		}
		return AuthenticatedUser.create(user);
	}

	@Transactional
	public AuthenticatedUser loadUserById(Long id) {

		User user = userRepository.findOne(id);
		if (user == null) {
			new EntityNotFoundException("User not found");
		}
		return AuthenticatedUser.create(user);
	}
}