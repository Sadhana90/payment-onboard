package com.paymentonboard.service;

import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.paymentonboard.entity.User;
import com.paymentonboard.repository.ResellerRepository;
import com.paymentonboard.repository.UserRepository;
import com.paymentonboard.util.GeneralUtil;
import com.paymentonboard.util.NotificationProperty;
import com.paymentonboard.util.SMSUtil;

import freemarker.template.TemplateException;



@Service
public class SelfOnboardService {
	private final Long expiryInterval = 5L * 60 * 1000;

	@Autowired
    private EmailService emailService;
	
	@Autowired
	private MerchantRepository merchantRepo;
	
	@Autowired
	private ResellerRepository resellerRepo;
	
	@Autowired
	private UserRepository userRepository;


	@Autowired
	GeneralUtil generalUtil;

	@Autowired
	SMSUtil smsUtil;

	@Autowired
	NotificationProperty notificationProperty;

	
	private static Logger log = LoggerFactory.getLogger(SelfOnboardService.class);
	
	public Map<String, Object> smsOTPconfigure(String MobileNo, String fullName, String emailId) {
		Map<String, Object> Msg = null ;
		String message = null;
		JSONObject js1 = new JSONObject();
		
		boolean result = MobileNo.matches("[7-9][0-9]{9}");
				
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		
		if(MobileNo != null && fullName != null && emailId != null || MobileNo.equals("") && fullName.equals("") && emailId.equals("")) {
//			String varMobileotp= smsUtil.generateOTP(false);
			String varEmailotp = smsUtil.generateOTP(false);
					if (result == true && matcher.matches() == true) {
						boolean userEmail = resellerRepo.findUserByEmail(emailId);
						if(userEmail == false) {
							
							// check latest entry get count.. check if want to generate OTP or want to restrict after 3 retry
							List<Object[]> lastOTPEntry = resellerRepo.findLastOTP(emailId,MobileNo);
							int retryCount = 0;
							if(lastOTPEntry!=null && !lastOTPEntry.isEmpty()) {
								Object[] otpDetail = lastOTPEntry.get(0);
								int currentRetryCount = Integer.parseInt(String.valueOf(otpDetail[1]));
								if(currentRetryCount>2) {
									message = "Max OTP generation attempt reached. Please contact customer support team.";
									js1.put("Status", "Error");
									js1.put("Message", message);
									return js1.toMap();
								}
								retryCount = Integer.parseInt(String.valueOf(otpDetail[1]))+1;
							}

							String resultEmail = emailSend(emailId,fullName , varEmailotp);
//							if (resultSms.equalsIgnoreCase("Success") && resultEmail.equalsIgnoreCase("Success")) {
							if (resultEmail.equalsIgnoreCase("Success")) {
								resellerRepo.insertSetailsSelfonBoard(fullName, emailId, MobileNo, null, varEmailotp,retryCount);
								message = "OTP has been sent to your Email ID";
								js1.put("Status", "Success");
								js1.put("Message", message);
							}else {
								message = "Problem Occure Sending OTP";
								js1.put("Status", "Error");
								js1.put("Message", message);
							}
						}else {
							message = "Email-Id is already Registered";
							js1.put("Status", "Error");
							js1.put("Message", message);
						}
				}else {
					if (result == false) {
						message = "Contact No is Invalid";
						js1.put("Status", "Error");
						js1.put("Message", message);
					}
					if (matcher.matches() == false) {
						message = "Email Id is Invalid";
						js1.put("Status", "Error");
						js1.put("Message", message);
					}
					if (result == false && matcher.matches() == false) {
						message = "Contact No & Email Id is Invalid";
						js1.put("Status", "Error");
						js1.put("Message", message);
					}
				}
			}else {
				message = "Empty Field Not Accepted";
				js1.put("Status", "Error");
				js1.put("Message", message);
			}
		Msg = js1.toMap();
		return Msg;
	}
	
	public Map<String, Object> getOTPVerified(String getEmailOTP) {
		Map<String, Object> Msg = null ;
		String message = null;
		JSONObject js1 = new JSONObject();
		
//		List<Object[]> getUsrinfo = resellerRepo.findbyOtp(getMobileOTP, getEmailOTP);
		List<Object[]> getUsrinfo = resellerRepo.findbyEmailOtp(getEmailOTP);
		
		if(!getUsrinfo.isEmpty()) {
			for(Object[] obj: getUsrinfo) 
            {
				String FullName = (String) obj[1];
				String emailId = (String) obj[2];
				String contactNo = (String) obj[3];
				Date createdon = (Date) obj[6];
				String vereified = (String) obj[7];
				LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);   
                long difference = timestamp.getTime()- createdon.getTime();
                LocalDateTime dateToLocalDateTime = LocalDateTime.ofInstant(createdon.toInstant(), java.time.ZoneId.systemDefault());
                Duration duration = Duration.ofMillis(difference);
                long seconds = duration.getSeconds();
                long minutesDifference = Duration.between(dateToLocalDateTime, now).toMinutes();

                System.out.println("Difference in minutes: " + minutesDifference);
                log.info("createdon is {} :", createdon);
                log.info("minutesDifference is {} :", minutesDifference);
                log.info("LocalDateTime is {} :", now);
                log.info("timestamp is {} :", timestamp);
                long MM = seconds / 60;
                log.info("MM is {} :", MM);
				if (minutesDifference >= 0 && minutesDifference <= 3) {
                	if (vereified.equals("0")) {
                		//String merchantId = merchantRepo.findMerchantId();
                		String firstName = FullName.contains(" ") ? FullName.split(" ")[0]: FullName;
                		String password = generalUtil.crypt((firstName.length() > 11 ? firstName.substring(0, 11) : firstName)
								+ "@" + new SimpleDateFormat("Y").format(new Date()));
                		User user = new User();
                		user.setFullName(FullName);
                		user.setBlocked(false);
                		user.setCreatedBy("Self");
                		user.setUserId(merchantId);
                		user.setMerchantId(merchantId);
                		user.setPassword(password);
                		user.setEmailId(emailId);
                		user.setGroupId(2);
                		user.setRoleId(2);
                		user.setIsActive(true);
                		user.setIsDeleted("N");
                		user.setContactNumber(contactNo);
                		user = userRepository.save(user);
                		String maxtoken = "0";
                		String status = "Self-Initiated";
                		Date date = Calendar.getInstance().getTime();                		
                		merchantRepo.createMerchantSelf(merchantId, FullName, emailId, contactNo, status, date, maxtoken);                		
             			String verified = "1";
             			merchantRepo.setSafeMode();
             			merchantRepo.updateOTPverifiedByEmail(getEmailOTP, verified);
             			String  resultEmail = emailSendonCreation(emailId,FullName, merchantId);
             			UserDetails userMer=this.customUserDetailsService.loadUserByUsername(merchantId);
             			String token = jwtTokenUtil.generateToken(userMer.getUsername());
                		 message = "OTP Verified";
                		 js1.put("Status", "Success");
 						 js1.put("Message", message);
 						 js1.put("data", merchantId);
 						 js1.put("Authorization", token);
                	 }else if(vereified.equals("1")) {
                		 message = "Your Account Already Verified";
                		 js1.put("Status", "Error");
             			 js1.put("Message", message);
                	 }else if(vereified.equals("2")) {
                		 message = "Your OTP Has Been Expired";
                		 js1.put("Status", "Error");
             			 js1.put("Message", message);
                	 }
                }else {
                	 message = "Your OTP Has Been Expired";
                	 js1.put("Status", "Error");
         			 js1.put("Message", message);
                	 if (vereified.equals("1") || vereified.equals("0") ){
	                	 String verified = "2";
	                	 merchantRepo.updateOTPverifiedByEmail(getEmailOTP, verified);                
                	}
                }
              }
			
		}else {
//			boolean mOTPVerfication = resellerRepo.findbyMOtp(getMobileOTP);
			boolean eOTPVerfication = resellerRepo.findbyEOtp(getEmailOTP);
			message = "";
//			if (mOTPVerfication == false) {
//				message = "Contact Number Verification OTP Is Wrong ";				
//			}
			if (eOTPVerfication == false) {
				message = message + " E-Mail Verification OTP Is Wrong";				
			}
			
			js1.put("Status", "Error");
			js1.put("Message", message);
		}
		
		Msg = js1.toMap();
		return Msg;
	} 
	
	
	public  Map<String, Object> createPasswordSelf(String newpassword, String JwtToken){
		Map<String, Object> Msg = null ;
		String message = null;
		JSONObject js1 = new JSONObject();
		List<Object[]> userId =  merchantRepo.findUserIdById(JwtToken);

		if(userId !=null) {	
			if (JwtToken !=null) {
				String passwordChangeEn = generalUtil.crypt(newpassword);
				merchantRepo.updateResetPass(JwtToken, passwordChangeEn);
					message = "Your Password Created Successfully and Your Merchant Id is " + JwtToken;
					js1.put("Status", "Success");
					js1.put("Message", message);
					
				}else {
					message = "Your Credential Is Not Matching";
					js1.put("Status", "Error");
					js1.put("Message", message);
				}
		}
		
		Msg = js1.toMap();
		return Msg;
	 } 
	
	public String emailSend(String emailId, String fullName, String otp) {
		String message = null;
		try {
			emailService.selfonboardEmail(emailId, fullName, otp );
			message ="Success";
		} catch (MessagingException e) {
			message ="Error";
			log.error("Error in emailSend : {}", e);
		} catch (IOException e) {
			message ="Error";
			log.error("Error in emailSend IOExcption: {}", e);
			
		} catch (TemplateException e) {
			message ="Error";
			log.error("Error in emailSend TemplateException: {0}", e);
			
		}
		
		return message; 
	}
	
	public String emailSendonCreation(String emailId, String fullName, String Mid) {
		String message = null;
		try {
			emailService.selfonboardUserCreation(emailId, fullName, Mid);
			message ="Success";
		} catch (MessagingException e) {
			message ="Error";
			log.error("Error in emailSend MessagingException : {0}", e);
		} catch (IOException e) {
			message ="Error";
			log.error("Error in emailSend IOException : {0}", e);
		} catch (TemplateException e) {
			message ="Error";
			log.error("Error in emailSend TemplateException : {0}", e);
		}
		
		return message; 
	}
}
