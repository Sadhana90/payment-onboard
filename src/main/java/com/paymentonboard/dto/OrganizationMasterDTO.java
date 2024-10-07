package com.paymentonboard.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OrganizationMasterDTO {
	private BigInteger id;
	private String orgName;
	private String orgGroupId;
	private Boolean isActive;
	private String address;
	private String contact;
	private String email;
	private String alias;
	private String logo;
	Theme theme;
	
	
}
