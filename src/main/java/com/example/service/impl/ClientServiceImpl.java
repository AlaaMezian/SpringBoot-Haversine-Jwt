package com.example.service.impl;

import static com.example.util.CommonsValidator.notEmpty;
import static com.example.util.CommonsValidator.validateEntity;
import static com.example.util.CommonsValidator.validateName;
import static com.example.util.CommonsValidator.validateNumber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.constant.Constants;
import com.example.entity.Client;
import com.example.exception.AuthorizationException;
import com.example.exception.BadRequestException;
import com.example.model.ClientModel;
import com.example.repository.ClientRepository;
import com.example.response.base.BaseResponse;
import com.example.response.base.ListResponse;
import com.example.response.base.SingleResponse;
import com.example.service.ClientService;
import com.example.util.CommonUtils;
import com.example.util.Haversine;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepo;

	@Override
	public BaseResponse createClient(ClientModel clientMdl) {
		validateName(clientMdl.getName());
		validateNumber(clientMdl.getLatitude(), "latitude");
		validateNumber(clientMdl.getLongitude(), "longitude");
		if (clientRepo.existsByPhoneNumber(clientMdl.getPhoneNumber())) {
			throw new BadRequestException("client with this phone number is already existed");
		}
		try {
			Client client = new Client();
			client.setClientName(clientMdl.getName());
			client.setLatitude(clientMdl.getLatitude());
			client.setLongitude(clientMdl.getLongitude());
			client.setPhoneNumber(clientMdl.getPhoneNumber());
			clientRepo.save(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseResponse.created("client created succesfully");
	}

	@Override
	public BaseResponse updateClient(long id, ClientModel clientMdl, HttpServletRequest request) {
		Client client = clientRepo.findOne(id);
		validateEntity(client, "client");
		long idFromToken = CommonUtils.getIdFromJWT(request);
		// to ensure the this update is done by the record owner
//		if (idFromToken != client.getId()) {
//			throw new AuthorizationException("Sorry you are not authorized to perform this action");
//		}

		if (notEmpty(clientMdl.getName())) {
			validateName(clientMdl.getName());
			client.setClientName(clientMdl.getName());
		}
		if (notEmpty(clientMdl.getLongitude())) {
			validateNumber(clientMdl.getLongitude(), "longitude");
			client.setLongitude(clientMdl.getLongitude());
		}
		if (notEmpty(clientMdl.getLatitude())) {
			validateNumber(clientMdl.getLatitude(), "latitude");
			client.setLatitude(clientMdl.getLatitude());
		}
		// phone number is unique
		if (notEmpty(clientMdl.getPhoneNumber())) {
			if (clientRepo.existsByPhoneNumber(clientMdl.getPhoneNumber())) {
				Client clinetFromDataBase = clientRepo.findByPhoneNumber(clientMdl.getPhoneNumber());
				if (clinetFromDataBase != client) {
					throw new BadRequestException("a user with this phone number is already exist");
				}
			}
			clientMdl.setPhoneNumber(clientMdl.getPhoneNumber());
		}
		clientRepo.save(client);
		return SingleResponse.updated(client);
	}

	@Override
	public BaseResponse deleteClient(long id) {
		Client client = clientRepo.findOne(id);
		validateEntity(client, "Client");
		client.setStatus(Constants.ENTITY_STATUS_NOT_ACTIVE);
		clientRepo.save(client);
		return BaseResponse.success("client deleted succesfully");
	}

	@Override
	public BaseResponse getAll() {

		List<Client> clients = clientRepo.findAllByActive(true);

		if (clients.size() == 0)
			return BaseResponse.noContent();

		return ListResponse.found(clients);
	}

	@Override
	public BaseResponse getById(long id) {
		Client client = clientRepo.findOne(id);
		validateEntity(client, "client");
		return SingleResponse.found(client);
	}

	@Override
	public BaseResponse findAllNearestClientWithIndistance(double latitude, double longtitude, double distance) {
		// we can do this in 2 way and i am going to demonstrate each one
		longtitude = checkLng(longtitude);
		latitude = checkLat(latitude);
		
		// first get nearest client to a user location with in distance from the data
		// base directly
		// apply haversine formula as a JPQL in the repository and that will return
		// clients with distance ordered from nearest to farthest like below

		List<Client> clients = clientRepo.findClientWithNearestLocation(latitude, longtitude, distance);
		
		if (clients.size() < 0)
			return BaseResponse.noContent();

		// or we can retrieve all clients and then apply haversine to calculate the
		// distance between the user and the client location and if it pass certain
		// criteria we will add it to and
		// list which will be returned as the final result

		List<Client> allActiveClients = clientRepo.findAllByActive(true);
		List<Client> nearestClients = new ArrayList<>();
		for (Client client : allActiveClients) {
			Double haversineDistance = Haversine.distance(latitude, longtitude, client.getLatitude(),
					client.getLongitude());
			if (distance >= haversineDistance) {
				nearestClients.add(client);
			}
		}

		if (nearestClients.isEmpty())
			return BaseResponse.noContent();

		return ListResponse.found(clients);
	}

	// if user send 0 long and 0 lat the default will ab amman down town
	private double checkLng(double longtitude) {
		if (longtitude == 0.0)
			longtitude = Double.parseDouble(Constants.LONG);
		return longtitude;
	}

	private double checkLat(double latitude) {
		if (latitude == 0.0)
			latitude = Double.parseDouble(Constants.LATT);
		return latitude;
	}

}
