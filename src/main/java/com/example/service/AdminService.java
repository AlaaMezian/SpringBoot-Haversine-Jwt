package com.example.service;

import javax.servlet.http.HttpServletRequest;

import com.example.response.base.BaseResponse;

public interface AdminService {
   public  BaseResponse createDefaultAdmin(HttpServletRequest request);
}
