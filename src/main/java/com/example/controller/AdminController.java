package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.response.base.BaseResponse;
import com.example.service.AdminService;

@RestController
@RequestMapping(value = "/api/v1")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	public BaseResponse createAdmin(HttpServletRequest request) {
		return adminService.createDefaultAdmin(request);
	}
}
