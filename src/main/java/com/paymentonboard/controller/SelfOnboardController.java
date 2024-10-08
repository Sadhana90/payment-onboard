package com.paymentonboard.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.paymentonboard.helper.JwtTokenUtil;
import com.paymentonboard.service.SelfOnboardService;

import lombok.extern.slf4j.Slf4j;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SelfOnboardController {
	@Autowired
	private SelfOnboardService selfOnboardService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
		
	@PostMapping("/get-Signup")
	public ResponseEntity<?> getSignup(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String mobileNo = js.getString("mobile");
		String fullName = js.getString("fullName");
		String emailId = js.getString("emailId");
		Map<String, Object> signupResponse= selfOnboardService.smsOTPconfigure(mobileNo,fullName,emailId);
		
		return ResponseEntity.ok(signupResponse);
	}
	
	@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/verified-OTP")
	public ResponseEntity<?> verifyOTP(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String getEmailOTP = js.getString("emailOTP");
		Map<String, Object> SignUpResponse= selfOnboardService.getOTPVerified( getEmailOTP );
		
		return ResponseEntity.ok("");
	} 
	
	@PostMapping("/create-passwordSelf")
	public ResponseEntity<?> createpasswordSelf(@RequestBody String jsonBody, @RequestHeader String Authorization) {
		JSONObject js = new JSONObject(jsonBody);
		JSONObject js1 = new JSONObject();
		String messageCtr = null;
		String newpassword = js.getString("newpassword");
		String JwtToken=null;
		Map<String, Object> responseMsg = null;
		JwtToken = Authorization.substring(7);
		try {
			String userId =jwtTokenUtil.getUsernameFromToken(JwtToken);
			 responseMsg= selfOnboardService.createPasswordSelf(newpassword,userId);	
		}catch(Exception e) {
			
			messageCtr = "Your Token Is Not Matching";
			js1.put("Status", "Error");
			js1.put("Message", messageCtr);
			responseMsg=js1.toMap();
			log.error("Error while create password self : {}", e.getMessage());
		}		
		return ResponseEntity.ok(responseMsg);
		
	}
	}
