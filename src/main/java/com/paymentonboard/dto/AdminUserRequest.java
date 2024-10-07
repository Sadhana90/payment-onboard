package com.paymentonboard.dto;

import lombok.Data;

@Data
public class AdminUserRequest {

    private String userId;

    private Integer roleId;

    private String fullName;

    private String password;

    private String contactNumber;

    private String emailId;
    
    private String action;

}