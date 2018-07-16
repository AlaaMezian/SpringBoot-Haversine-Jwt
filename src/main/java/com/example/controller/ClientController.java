package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.ClientModel;
import com.example.response.base.BaseResponse;
import com.example.service.ClientService;

@RestController
@RequestMapping(value = "/api/v1")
public class ClientController {

	@Autowired
	private ClientService clientService;

	// only user logged in to the system can retrieve all clients
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public BaseResponse getAllClients() {
		return clientService.getAll();
	}

	// only admin can create clients
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/client", method = RequestMethod.POST)
	public BaseResponse createClient(@RequestBody ClientModel clientMdl) {
		return clientService.createClient(clientMdl);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/client/{id}", method = RequestMethod.PUT)
	public BaseResponse updateClient(@RequestBody ClientModel clientMdl, @PathVariable long id,
			HttpServletRequest request) {
		return clientService.updateClient(id, clientMdl, request);
	}

	// only admin can delete clients
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/client/{id}", method = RequestMethod.DELETE)
	public BaseResponse deleteClient(@PathVariable long id) {
		return clientService.deleteClient(id);
	}

	// only users and admin can get client by id
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
	public BaseResponse getClient(@PathVariable long id) {
		return clientService.getById(id);
	}
	// only logged in user can get nearest client
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@RequestMapping(value = "/nearestClient", method = RequestMethod.GET)
	public BaseResponse getNearestClient(@RequestParam(value = "distance") double distance,
			@RequestParam(value = "longitude") double longitude, @RequestParam(value = "latitude") double latitude,
			HttpServletRequest request) {
		return clientService.findAllNearestClientWithIndistance(latitude ,longitude ,distance);
	}
}
