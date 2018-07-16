package com.example.service;

import javax.servlet.http.HttpServletRequest;

import com.example.model.ClientModel;
import com.example.response.base.BaseResponse;

public interface ClientService {

	BaseResponse createClient(ClientModel clientMdl);

	BaseResponse updateClient(long id, ClientModel clientMdl, HttpServletRequest request);

	BaseResponse deleteClient(long id);

	BaseResponse getAll();

	BaseResponse getById(long id);

	BaseResponse findAllNearestClientWithIndistance(double latitude, double longtitude, double distance);
}
