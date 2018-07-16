package com.example.service.impl;

import static com.example.util.CommonsValidator.isValidUserName;
import static com.example.util.CommonsValidator.validateEmail;
import static com.example.util.CommonsValidator.validatePassword;
import static com.example.util.CommonsValidator.validateString;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.constant.RoleName;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.model.LoginModel;
import com.example.model.SignUpModel;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.response.base.BaseResponse;
import com.example.response.base.JwtAuthenticationResponse;
import com.example.security.AuthenticatedUser;
import com.example.security.JwtTokenProvider;
import com.example.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	// instantiating a bean to use password encoder
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Override
	public BaseResponse signUp(SignUpModel signUpRequest) {

		validatePassword(signUpRequest.getPassword(), "password");
		isValidUserName(signUpRequest.getUsername());
		validateEmail(signUpRequest.getEmail());
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new BadRequestException("UserName is already taken!");
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("email is already taken																							!");
		}

		// Creating user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getEmail());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new BadRequestException("User Role not set."));

		user.setRole(userRole);

		userRepository.save(user);
		return BaseResponse.created("user signed Up Successfully");

	}

	@Override
	public JwtAuthenticationResponse logIn(LoginModel loginRequest, HttpServletRequest req) {
		validateString(loginRequest.getUsernameOrEmail(), "usernameOrEmail");
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadRequestException("Bad Credintials");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// from the authenticated user we can access all current authenticated user data
																																																																																																																																																																																																																																																																																																																																																																																	// such as id for example
		Object userPrinciple = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AuthenticatedUser user = ((AuthenticatedUser) userPrinciple);

		String jwt = tokenProvider.generateToken(authentication);
		return new JwtAuthenticationResponse(jwt);
	}
}
