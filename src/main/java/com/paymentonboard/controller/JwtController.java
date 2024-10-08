package com.paymentonboard.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymentonboard.dto.TokenReq;
import com.paymentonboard.dto.UserRequest;
import com.paymentonboard.entity.JwtTokenInfo;
import com.paymentonboard.entity.User;
import com.paymentonboard.helper.JwtTokenUtil;
import com.paymentonboard.repository.JwtTokenInfoRepository;
import com.paymentonboard.service.CustomUserDetailsService;
import com.paymentonboard.util.GeneralUtil;

import antlr.StringUtils;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://pa-preprod.1pay.in"})
public class JwtController {
	

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    GeneralUtil generalUtil;

    //	String defualtLogo = "/home/KYCDOCUMENTS/LogoFolderPanvel/defualt.png";
    String fileUploadLocation = "/home/KYCDOCUMENTS/LogoFolderPanvel/defualt.png";

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtTokenInfoRepository jwtTokenInfoRepository;

    @Autowired
    private AuthenticationManager authenticationManger;

    @Autowired
    private com.paymentonboard.repository.UserRepository UserRepository;

    @Autowired
    private com.paymentonboard.service.CommonService CommonService;

    @Autowired
    private UserRequest user2;

    @Autowired
    private UserRequest user1;

    
    private static String ERROR_MESSAGE = "No Access! Contact Administrator";
    
 

    @CrossOrigin(origins = {"http://localhost:4200", "https://pa-preprod.1pay.in"})
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody UserRequest userRequest) throws Exception {

        User userMst = UserRepository.findByUserId(userRequest.getUsername()).orElseThrow(() -> new Exception("User not found for userId"));

        try {
            if (userMst.getLockTime() != null) {
                Duration duration = Duration.between(userMst.getLockTime(), LocalDateTime.now());
                if (Boolean.TRUE.equals(userMst.getAccLock()) && duration.toMinutes() <= 15) {
                	TokenReq tokenReq = new TokenReq();
                    tokenReq.setSuccess(false);
                    tokenReq.setErrorMessage("Your account has been locked due to exceeding login attempts. Please try after 15 mins.");
                    return ResponseEntity.ok(tokenReq);
                } else {
                    setNullValueOfUserAccLock(userMst);
                }
            }
            this.authenticationManger.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
        } catch (UsernameNotFoundException | BadCredentialsException e) {

            if (userMst != null) {
                int wrongAttemptCount = userMst.getWrongAttemptCount() != null ? userMst.getWrongAttemptCount() : 0;
                if (userMst.getAccLock() == null || Boolean.FALSE.equals(userMst.getAccLock())) {
                    userMst.setWrongAttemptCount(wrongAttemptCount + 1);
                    if (userMst.getWrongAttemptCount() == 3) {
                        userMst.setAccLock(true);
                        userMst.setLockTime(LocalDateTime.now());
                    }
                    UserRepository.save(userMst);
                }
            }
            throw e;
        }
        
        UserRequest userInfo = new UserRequest();
        userInfo.setUsername(userMst.getUserId());
        userInfo = CommonService.userLoginDetails(userInfo);
        
        log.info("UserRequest : {} and userInfo : {}", userRequest, userInfo);

		if (userRequest.getUrlShortName() != null && !userRequest.getUrlShortName().equals(userInfo.getShortname())) {
			TokenReq tokenReq = new TokenReq();
            tokenReq.setSuccess(false);
//            tokenReq.setErrorMessage("Please send correct url short name.");
            tokenReq.setErrorMessage(ERROR_MESSAGE);
            return ResponseEntity.ok(tokenReq);
		} else if (userRequest.getUrlShortName() == null && userInfo.getPersonalizedUrl() != null && userInfo.getPersonalizedUrl().equals("1")) {
			TokenReq tokenReq = new TokenReq();
            tokenReq.setSuccess(false);
//            tokenReq.setErrorMessage("Short name is required for this User.");
            tokenReq.setErrorMessage(ERROR_MESSAGE);
            return ResponseEntity.ok(tokenReq);
		}
        
        String token = jwtTokenUtil.generateToken(userMst.getUserId());

        JwtTokenInfo jwtTokenInfo = new JwtTokenInfo();
        jwtTokenInfo.setToken(token);
        jwtTokenInfo.setIsActive(Boolean.TRUE);
        jwtTokenInfo.setCreatedOn(LocalDateTime.now());
        jwtTokenInfoRepository.save(jwtTokenInfo);

        setNullValueOfUserAccLock(userMst);

        TokenReq tokenReq = new TokenReq();
        tokenReq.setToken(token);
        tokenReq.setRoleId(Long.parseLong(userInfo.getROLEID()));
        return ResponseEntity.ok(tokenReq);
    }
    
    private void setNullValueOfUserAccLock(User userMst) {
        userMst.setAccLock(null);
        userMst.setWrongAttemptCount(null);
        userMst.setLockTime(null);
        UserRepository.save(userMst);
    }


    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping(value = "/GetDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMenuSubMenu(HttpServletRequest request) throws Exception {

        String RequestTokenHeader = request.getHeader("Authorization");
        log.info("JwtAuthFilter.java::::::::::::::;Token is Validated" + RequestTokenHeader);
        String Username = null;
        String JwtToken = null;
        if (RequestTokenHeader != null && RequestTokenHeader.startsWith("Bearer ")) {
            JwtToken = RequestTokenHeader.substring(7);
        }
        List<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<>();
        Username = jwtTokenUtil.getUsernameFromToken(JwtToken);
        user1.setUsername(Username);
        user2 = CommonService.userLoginDetails(user1);
        String menus = this.customUserDetailsService.getMenuHierarchy(user2.getROLEID());
        JSONObject js1 = new JSONObject(user2);
        JSONObject js2 = new JSONObject(menus);

        js1.remove("password");
        /*
         * JSONArray jss = new JSONArray(menus);
         */
        js1.put("Menu", js2);
        return ResponseEntity.ok(js1.toMap());


    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping(value = "/GetDetailsApi", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMenusAccess(HttpServletRequest request, @RequestHeader("x-request-src") String authString, @RequestParam(name = "merchantId") Optional<String> merchantId) throws Exception {

        String RequestTokenHeader = request.getHeader("Authorization");
        log.info("JwtAuthFilter.java::::::::::::::;Token is Validated" + RequestTokenHeader);
        String Username = null;
        String JwtToken = null;
        JSONObject js1;


        try {
            if (RequestTokenHeader != null && RequestTokenHeader.startsWith("Bearer ")) {
                JwtToken = RequestTokenHeader.substring(7);
            }

            String userId = null;
            String merchantUser = null;
            if(merchantId.isPresent() && !merchantId.get().isBlank() && !merchantId.get().isEmpty()) {
                // USER login
            	userId  = jwtTokenUtil.getUsernameFromToken(JwtToken);
            	Username = jwtTokenUtil.getUsernameFromToken(JwtToken);
            	merchantUser = merchantId.get();
            	 user1.setUsername(Username);
                 user2 = CommonService.userLoginDetails(user1);
                 user2.setMerchantId(merchantId.get());
            } else {
            	userId = jwtTokenUtil.getUsernameFromToken(JwtToken);
            	merchantUser =  UserRepository.findByUserId(userId).get().getMerchantId();
            	Username = userId;
                 user1.setUsername(Username);
                 user2 = CommonService.userLoginDetails(user1);
//            	Username = merchantUser == null || merchantUser.isBlank() || merchantUser.isEmpty() ? userId  : merchantUser;
            }

            log.info("Username:::::::::::::::::::{} ",
                    Username);
           
            ArrayList menus = this.customUserDetailsService.getMenusHirarchy(user2.getROLEID(), merchantUser, userId);
            
            user2.setUSERID(userId);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String str = formatter.format(date);
            log.info("Current date: {}", str);
            user2.setCurrentDate(str);
            js1 = new JSONObject(user2);
            JSONObject js2 = new JSONObject();
            js1.remove("password");

            /*
             * JSONArray jss = new JSONArray(menus);
             *
             *
             */


            js1.put("Menu", menus);


            log.info("Username:::::::::::::: {}", Username);
//				  boolean MerchantVerify = Username.startsWith("M00");
            GeneralUtil generalUtil = new GeneralUtil();
            String UserType = generalUtil.isUserMatching(authString);

//				  if(MerchantVerify == true) {
            String mainMerchantOrResellerId = Username.contains("_") ? merchantUser : Username;
            if (UserType.equalsIgnoreCase("Merchant")) {
                log.info("MerchantVerify::::true::::::::::{}", UserType);

                List<Object[]> verificationLogo = UserRepository.findVerificationLogo(mainMerchantOrResellerId);

                for (Object[] obj : verificationLogo) {
                    String logopath = ((String) obj[0]);
                    String logotype = ((String) obj[1]);
                    if (logotype.equals("1") || logotype.equals("3")) {
                        if (!logopath.equalsIgnoreCase("")) {
                            String defaultLogo = logopath;
                            log.info("defaultLogo:::::::::{}", defaultLogo);
                            String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                            log.info("s1:::::::{}", s1);
                            s1.trim();
                            Path path = Paths.get(defaultLogo);
                            File file = new File(defaultLogo);
                            byte[] fileContent = null;
                            try {
                                fileContent = Files.readAllBytes(file.toPath());
                                js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                            } catch (Exception e) {

                                js1.put("Data", "");
                                log.error("Error :: {0}", e);
                            }
                        } else {
                            String defaultLogo = fileUploadLocation;
                            log.info("defaultLogo:::::::::{}", defaultLogo);
                            String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                            log.info("s1:::::::{}", s1);
                            s1.trim();
                            Path path = Paths.get(defaultLogo);
                            File file = new File(defaultLogo);
                            byte[] fileContent = null;
                            try {
                                fileContent = Files.readAllBytes(file.toPath());
                                js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                            } catch (Exception e) {

                                js1.put("Data", "");
                                log.error("Error :: {0}", e);
                            }
                        }
                    } else if (logotype.equals("2") || logotype.equals("4") || logotype.equalsIgnoreCase("") || logopath.equalsIgnoreCase("")) {
                        String defaultLogo = fileUploadLocation;
                        log.info("defaultLogo:::::::::{}", defaultLogo);
                        String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                        log.info("s1:::::::{}", s1);
                        s1.trim();
                        Path path = Paths.get(defaultLogo);
                        File file = new File(defaultLogo);
                        byte[] fileContent = null;
                        try {
                            fileContent = Files.readAllBytes(file.toPath());
                            js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                        } catch (Exception e) {

                            js1.put("Data", "");
                            log.error("Error :: {0}", e);
                        }
                    }

                }


            } else if (UserType.equalsIgnoreCase("Aggregator")) {
                List<Object[]> verificationLogo = UserRepository.findaggregatorLogo(mainMerchantOrResellerId);

                for (Object[] obj : verificationLogo) {
                    String logopath = ((String) obj[0]);
                    String logotype = ((String) obj[1]);
                    if (logotype.equals("1") || logotype.equals("3")) {
                        if (!logopath.equalsIgnoreCase("")) {
                            String defaultLogo = logopath;
                            log.info("defaultLogo:::::::::{}", defaultLogo);
                            String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                            log.info("s1:::::::{}", s1);
                            s1.trim();
                            Path path = Paths.get(defaultLogo);
                            File file = new File(defaultLogo);
                            byte[] fileContent = null;
                            try {
                                fileContent = Files.readAllBytes(file.toPath());
                                js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                            } catch (Exception e) {

                                js1.put("Data", "");
                                log.error("Error :: {0}", e);
                            }

                        } else {
                            String defaultLogo = fileUploadLocation;
                            log.info("defaultLogo:::::::::{}", defaultLogo);
                            String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                            log.info("s1:::::::{}", s1);
                            s1.trim();
                            Path path = Paths.get(defaultLogo);
                            File file = new File(defaultLogo);
                            byte[] fileContent = null;
                            try {
                                fileContent = Files.readAllBytes(file.toPath());
                                js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                            } catch (Exception e) {

                                js1.put("Data", "");
                                log.error("Error :: {0}", e);
                            }
                        }
                    } else if (logotype.equals("2") || logotype.equals("4") || logotype.equalsIgnoreCase("") || logopath.equalsIgnoreCase("")) {
                        String defaultLogo = fileUploadLocation;
                        log.info("defaultLogo:::::::::{}", defaultLogo);
                        String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                        log.info("s1:::::::{}", s1);
                        s1.trim();
                        Path path = Paths.get(defaultLogo);
                        File file = new File(defaultLogo);
                        byte[] fileContent = null;
                        try {
                            fileContent = Files.readAllBytes(file.toPath());
                            js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                        } catch (Exception e) {

                            js1.put("Data", "");
                            log.error("Error :: {0}", e);
                        }
                    }

                }

            } else if (UserType.equalsIgnoreCase("Admin")) {
                String defaultLogo = fileUploadLocation;
                log.info("defaultLogo:::::::::{}", defaultLogo);
                String s1 = defaultLogo.substring(defaultLogo.lastIndexOf(".") + 1);
                log.info("s1:::::::{}", s1);
                s1.trim();
                Path path = Paths.get(defaultLogo);
                File file = new File(defaultLogo);
                byte[] fileContent = null;
                try {
                    fileContent = Files.readAllBytes(file.toPath());
                    js1.put("Data", "data:image/" + s1 + ";base64," + Base64.getEncoder().encodeToString(fileContent));
                } catch (Exception e) {
                    js1.put("Data", "");
                    log.error("Error :: {0}", e);
                }
            }

            return ResponseEntity.ok(js1.toMap());
        } catch (Exception e) {
            log.info("Exception e:::::::::::::::::: ", e);

            return new ResponseEntity<Object>("Unauthrized Acces", HttpStatus.UNAUTHORIZED);
        }


    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @PostMapping(value = "/CheckPermission", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> CheckPermssion(@RequestBody String fields) throws Exception {
        boolean Status;
        JSONObject js = new JSONObject(fields);
        String Role_Id = js.getString("Role_id");
        String ButtonValue = js.getString("Permission");
        int i = customUserDetailsService.CheckPermission(Role_Id, ButtonValue);
        if (i <= 0) {
            Status = false;
        } else {
            Status = true;
        }
        JSONObject Staus = new JSONObject();
        Staus.put("Status", Status);

        return ResponseEntity.ok(Staus.toMap());

    }

}
	
	

