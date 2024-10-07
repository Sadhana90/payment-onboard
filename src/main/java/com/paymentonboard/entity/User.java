package com.paymentonboard.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.paymentonboard.dto.AdminUserRequest;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_mstuser")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "USERID")
    private String userId;

    @Column(name = "GROUPID")
    private Integer groupId;

    @Column(name = "ROLEID")
    private Integer roleId;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "Created_On")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Modified_On")
    private LocalDateTime modifiedOn;

    @Column(name="Modified_By")
    private String modifiedBy;

    @Column(name = "Is_Deleted")
    private String isDeleted = "N";

    @Column(name = "ContactNo")
    private String contactNumber;

    @Column(name = "EmailId")
    private String emailId;

    @Column(name = "MerchantId")
    private String merchantId;

    @Column(name = "blocked")
    private Boolean blocked = Boolean.FALSE;
    
    @Column(name = "wrong_attempt_count")
    private Integer wrongAttemptCount;
    
    @Column(name = "acc_lock")
    private Boolean accLock = Boolean.FALSE;

    @Column(name = "lock_time")
	private LocalDateTime lockTime;
    
    public void setUserRequestValues(AdminUserRequest userRequest){
        this.userId = userRequest.getUserId();
        this.roleId = userRequest.getRoleId();
        this.fullName = userRequest.getFullName();
        this.password = userRequest.getPassword();
        this.contactNumber = userRequest.getContactNumber();
        this.emailId = userRequest.getEmailId();
    }



    @Override
    public String toString() {
        return "User [userId=" + userId + ", groupId=" + groupId + ", roleId=" + roleId + ", fullName=" + fullName
                + ", password=" + password + ", isActive=" + isActive + ", createdOn=" + createdOn + ", createdBy="
                + createdBy + ", isDeleted=" + isDeleted + ", contactNumber=" + contactNumber + ", emailId=" + emailId
                + ", merchantId=" + merchantId + ", blocked=" + blocked + "]";
    }


}
