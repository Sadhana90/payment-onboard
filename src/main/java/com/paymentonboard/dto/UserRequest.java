package com.paymentonboard.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserRequest {

    String username;
    String password;
    String USERID;
    String GROUPID;
    String ROLEID;
    String FullName;
    String ContactNo;
    String EmailId;
    String CurrentDate;
    String userType;
    String menus;
    String shortname;
    String urlShortName;
    String id;
    String MerchantId;
    String personalizedUrl;

}

