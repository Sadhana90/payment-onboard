package com.paymentonboard.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_selfonboard")
public class SelfOnboard implements Serializable{

	 @Id
	 @Column(name = "id")
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer Id;
	 
		
		  @Column(name = "fullName") 
		  private String fullName;
		  
		  @Column(name = "emailId") 
		  private String emailId;
		  
		  @Column(name = "contactNo") private String contactNo;
		  
		  @Column(name = "otpNumMobile") private String otpNumMobile;
		  
		  @Column(name = "otpNumEmail") private String otpNumEmail;
		  
		  @Column(name = "createdOn") private Date createdOn;
		  
		  @Column(name = "verified") private String verified;
		  
		  @Column(name = "otpRetryCount") private String otpRetryCount;
		 
}
