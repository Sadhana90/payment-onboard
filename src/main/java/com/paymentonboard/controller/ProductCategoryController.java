package com.paymentonboard.controller;

import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.paymentonboard.service.ProductService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductCategoryController {

	@Autowired
	private ProductService productService;
	
	@GetMapping(value = "/getProductDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getProductCategoryData() {
		JSONArray menuArray= productService.getProductCategoryData();
        return new ResponseEntity<>(menuArray.toString(), HttpStatus.OK);

	}
}
