package com.paymentonboard.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.dto.Theme;
import com.paymentonboard.entity.OrganizationMaster;
import com.paymentonboard.repository.OrganizationRepository;
import com.paymentonboard.service.OrganizationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrganizationMasterServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository orgMasterRepository;

	@Override
	public OrganizationMasterDTO getOrganizationData(String org) {
		log.info("org {}", org);
		// TODO Auto-generated method stub
		List<Object[]> list= orgMasterRepository.findOrgDetails(org, true);
		OrganizationMasterDTO organizationMasterDTO=  new OrganizationMasterDTO();
		for (Object[] obj : list) {
			organizationMasterDTO.setId((BigInteger) obj[0]);
			organizationMasterDTO.setOrgName((String) obj[1]);
			organizationMasterDTO.setOrgGroupId((String) obj[2]);
			organizationMasterDTO.setIsActive((Boolean) obj[3]);
			organizationMasterDTO.setAddress((String) obj[4]);
			organizationMasterDTO.setContact((String) obj[5]);
			organizationMasterDTO.setEmail((String) obj[6]);
			organizationMasterDTO.setAlias((String) obj[7]);
			organizationMasterDTO.setLogo((String) obj[8]);
			Theme theme= new Theme();
			theme.setDark_colour((String) obj[9]);
			theme.setLight_colour((String) obj[10]);
			theme.setPrimary_colour((String) obj[11]);
			theme.setSecondary_colour((String) obj[12]);
			theme.setTertiary_colour((String) obj[13]);
			organizationMasterDTO.setTheme(theme);

		}
		return organizationMasterDTO;
	}
}