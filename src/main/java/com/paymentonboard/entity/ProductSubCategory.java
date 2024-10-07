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
@Table(name = "product_subcategory_mst")
@Data
public class ProductSubCategory{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "productsubcategory")
	private String productSubCategory;
	
	@Column(name = "isactive")
	private Boolean isactive;
	
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "productid")
	private Long productid;
}

