package com.paymentonboard.service;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.json.JSONArray;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.entity.OrganizationMaster;


public interface ProductService {
	JSONArray getProductCategoryData();
}
