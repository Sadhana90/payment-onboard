package com.paymentonboard.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;



@Entity
@Table(name = "organization_mst")
@Data
public class OrganizationMaster{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "org_name")
	private String orgName;
	
	@Column(name = "org_group_id")
	private String orgGroupId;
	
	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "address")
	private String address;
	
	@Column(name = "contact")
	private String contact;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "alias")
	private String alias;
	
	@Column(name = "logo")
	private String logo;
	
	/*@Column(name = "createdon")
	private Timestamp createdon;

	@Column(name = "createdby")
	private String createdby;
	
	@Column(name = "updatedon")
	private Timestamp updatedon;
	
	@Column(name = "updatedby")
	private String updatedby; */
}