package com.paymentonboard.service;

import java.util.List;

import javax.persistence.Tuple;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.entity.OrganizationMaster;


public interface OrganizationService {
	OrganizationMasterDTO getOrganizationData(String org);
}
