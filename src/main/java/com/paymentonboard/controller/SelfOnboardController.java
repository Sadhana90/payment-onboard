package com.paymentonboard.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymentonboard.service.SelfOnboardService;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SelfOnboardController {
	@Autowired
	private SelfOnboardService selfOnboardService;
		
	@PostMapping("/get-Signup")
	public ResponseEntity<?> getSignup(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
		String mobileNo = js.getString("mobile");
		String fullName = js.getString("fullName");
		String emailId = js.getString("emailId");
		Map<String, Object> signupResponse= selfOnboardService.smsOTPconfigure(mobileNo,fullName,emailId);
		
		return ResponseEntity.ok(signupResponse);
	}
	
	/*@CrossOrigin(origins = { "http://localhost:4200", "https://pa-preprod.1pay.in" })
	@PostMapping("/verified-OTP")
	public ResponseEntity<?> verifyOTP(@RequestBody String jsonBody) {
		JSONObject js = new JSONObject(jsonBody);
//		String getMobileOTP = js.getString("mobileOTP");
		String getEmailOTP = js.getString("emailOTP");
//		Map<String, Object> SignUpResponse= selfOnboardService.getOTPVerified(getMobileOTP, getEmailOTP );
//		Map<String, Object> SignUpResponse= selfOnboardService.getOTPVerified( getEmailOTP );
		
		return ResponseEntity.ok("");
	} */
	
/*	@PostMapping("/create-passwordSelf")
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
		*/
		
	}
