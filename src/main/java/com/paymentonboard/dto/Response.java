package com.paymentonboard.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

	private String statusCode;// Application level identification code of error.
	private boolean success; // Success flag for operation
	private String message;// String response message for the cases which does't have data to
	private Object data;
	private Map<String, String> errors;

	public Response(boolean success, String statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.success = success;
		this.message = message;
	}

	public Response(boolean success, Map<String, String> errors) {
		super();
		this.success = success;
		this.errors = errors;
	}

	public Response(boolean success, Object data) {
		super();
		this.success = success;
		this.data = data;
	}
	
	public Response(boolean success, String statusCode, Object data) {
		super();
		this.success = success;
		this.data = data;
		this.statusCode = statusCode;
	}

	public Response(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public Response(boolean success) {
		super();
		this.success = success;
		this.message = "SUCCESS";
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}
