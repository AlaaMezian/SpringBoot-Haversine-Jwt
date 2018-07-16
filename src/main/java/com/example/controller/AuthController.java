package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.LoginModel;
import com.example.model.SignUpModel;
import com.example.response.base.BaseResponse;
import com.example.service.AuthService;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginModel loginRequest, HttpServletRequest req) {

		return ResponseEntity.ok(authService.logIn(loginRequest, req));
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public BaseResponse registerUser(@Valid @RequestBody SignUpModel signUpRequest) {
		return authService.signUp(signUpRequest);
	}

}
