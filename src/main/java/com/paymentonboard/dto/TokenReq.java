package com.paymentonboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TokenReq {

    String Token="";
    String RefreshToken="";
    Boolean success=true;
    String errorMessage;
	Long roleId;
	
    
    
	public TokenReq() {
		// TODO Auto-generated constructor stub
	}
	public TokenReq(String token) {
		super();
		Token = token;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getRefreshToken() {
		return RefreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		RefreshToken = refreshToken;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "TokenReq [Token=" + Token + ", RefreshToken=" + RefreshToken + "]";
	}
    
    
	
	
}
