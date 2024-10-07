package com.paymentonboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.dto.Response;
import com.paymentonboard.service.OrganizationService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;
	
	@GetMapping("/getOrganization/{org}")
	public ResponseEntity<Response> getOrganizationData(@PathVariable String org) {
		return new ResponseEntity<>(new Response(true, organizationService.getOrganizationData(org)), HttpStatus.OK);
	}
}
