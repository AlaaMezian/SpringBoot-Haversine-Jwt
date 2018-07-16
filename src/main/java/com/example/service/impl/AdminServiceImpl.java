package com.example.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.constant.RoleName;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.response.base.BaseResponse;
import com.example.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepo;
	
	@Bean
	public BCryptPasswordEncoder bryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public BaseResponse createDefaultAdmin(HttpServletRequest request) {

		User user = new User();
		if (userRepo.count() == 0) {
			user.setEmail("admin@gmail.com");
			user.setPassword(passwordEncoder.encode("123456789"));
			user.setUsername("mainAdmin");
			user.setFirstName("Admin");
			user.setLastName("Admin");

			Role userRole = roleRepo.findByName(RoleName.ROLE_ADMIN)
					.orElseThrow(() -> new BadRequestException("User Role not set."));

			user.setRole(userRole);
			userRepo.save(user);
			return BaseResponse.created("default admin created successfully");
		}
		return BaseResponse.failed("a default admin already existed");
	}

}
