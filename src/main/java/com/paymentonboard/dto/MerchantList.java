package com.paymentonboard.dto;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_mstmerchant")
public class MerchantList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MerchantId")
    private String merchantId;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mer_return_url")
    private String merchantReturnUrl;

    @Column(name = "isretryAllowed")
    private boolean isRetryAllowed = false;

    @Column(name = "is_auto_refund")
    private String isAutoRefund;

    @Column(name = "hours")
    private String hours;

    @Column(name = "minutes")
    private String minutes;

    @Column(name = "is_push_url")
    private String isPushUrl;

    @Column(name = "integration_type")
    private String integrationType;

    @Column(name = "is_surcharge")
    private String isSurcharge;

    @Column(name = "is_vas")
    private String isVas;

    @Column(name = "transaction_key")
    private String transactionKey;

    @Column(name = "mer_website_url")
    private String merchantWebsiteUrl;

    @Column(name = "merchant_category_code")
    private String merchantCategoryCode;

    @Column(name = "merchant_sub_category")
    private String merchantSubCategory;

    @Column(name = "merchant_status")
    private Integer merchantStatus = 0;

    @Column(name = "max_token_size")
    private String maxTokenSize;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "lead_id")
    private Integer leadId = 0;

    @Column(name = "reseller_id")
    private String resellerId;

    @Column(name = "rodt")
    private LocalDateTime rodt;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "transkey")
    private String transKey;

    @Column(name = "website")
    private String website;

    @Column(name = "disable_refund_cc")
    private String disableRefundCc;

    @Column(name = "disable_refund_dc")
    private String disableRefundDc;

    @Column(name = "disable_refund_nb")
    private String disableRefundNb;

    @Column(name = "disable_refund_upi")
    private String disableRefundUpi;

    @Column(name = "disable_refund_wallet")
    private String disableRefundWallet;

    @Column(name = "Is_deleted")
    private String isDeleted;

    @Column(name = "push_url")
    private String pushUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "is_allowedBins")
    private String isAllowedBins;

    @Column(name = "ibps_mail_remainder")
    private String ibpsMailRemainder;

    @Column(name = "merchant_dashboard_refund")
    private String merchantDashboardRefund;

    @Column(name = "md_disable_refund_cc")
    private String mdDisableRefundCc;

    @Column(name = "md_disable_refund_dc")
    private String mdDisableRefundDc;

    @Column(name = "md_disable_refund_nb")
    private String mdDisableRefundNb;

    @Column(name = "md_disable_refund_upi")
    private String mdDisableRefundUpi;

    @Column(name = "md_disable_refund_wallet")
    private String mdDisableRefundWallet;

    @Column(name = "refund_api")
    private String refundApi;

    @Column(name = "refund_api_disable_cc")
    private String refundApiDisableCc;

    @Column(name = "refund_api_disable_dc")
    private String refundApiDisableDc;

    @Column(name = "refund_api_disable_nb")
    private String refundApiDisableNb;

    @Column(name = "refund_api_disable_upi")
    private String refundApiDisableUpi;

    @Column(name = "refund_api_disable_wallet")
    private String refundApiDisableWallet;

    @Column(name = "ibps_email_notification")
    private String ibpsEmailNotification;

    @Column(name = "ibps_sms_notification")
    private String ibpsSmsNotification;

    @Column(name = "enable_tpv")
    private String enableTpv;

    @Column(name = "settlement_cycle")
    private String settlementCycle;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "address")
    private String address;

    @Column(name = "CompanyPAN")
    private String companyPan;

    @Column(name = "BusinessType")
    private String businessType;

    @Column(name = "DateofIncorporation")
    private String dateOfIncorporation;

    @Column(name = "businessModel")
    private String businessModel;

    @Column(name = "TurnoverinlastFinancialYear")
    private String turnOverInLastFinancialYear;

    @Column(name = "ExpectedMonthlyIncome")
    private String expectedMonthlyIncome;

    @Column(name = "AverageAmountPerTransaction")
    private String averageAmountPerTransaction;

    @Column(name = "AuthorisedSignatoryPAN")
    private String authorisedSignatoryPan;

    @Column(name = "NameAsPerPAN")
    private String nameAsPerPan;

    @Column(name = "GSTINNo")
    private String gstInNo;

    @Column(name = "IsTestAccess")
    private String isTestAccess;

    @Column(name = "Instruments")
    private String instruments;

    @Column(name = "IsPANVerified")
    private String isPanVerified;

    @Column(name = "IsGSTVerify")
    private String isGstVerify;

    @Column(name = "CompanyPanVerifyName")
    private String companyPanVerifyName;

    @Column(name = "GSTVerifyName")
    private String gstVerifyName;

    @Column(name = "IsCompanyPanVerify")
    private String isCompanyPanVerify;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "Reporting_cycle")
    private String reportingCycle;

    @Column(name = "is_save_card")
    private String isSaveCard;

    @Column(name = "risk_approvel")
    private Integer riskApprovel;

    @Column(name = "kyc_approvel")
    private Integer kycApprovel;

    @Column(name = "oprations_approvel")
    private Integer operationsApprovel;

    @Column(name = "SuspendedDate")
    private String suspendedDate;

    @Column(name = "pre_settlement")
    private String preSettlement;

    @Column(name = "res_fieldsdetails")
    private String resFieldsDetails;

    @Column(name = "is_loader_access")
    private String isLoaderAccess;

    @Column(name = "upi_loader")
    private String upiLoader;

    @Column(name = "email_cred_triggered")
    private String emailCredTriggered;

    @Column(name = "upi_intent")
    private String upiIntent;

    @Column(name = "upi_collect")
    private String upiCollect;

    @Column(name = "additional_contact")
    private String additionalContact;

    @Column(name = "settlement_group")
    private String settlementGroup;

    @Column(name = "Is_selfOnboarded")
    private String isSelfOnboarded;

    @Column(name = "sales_lead")
    private String salesLead;

    @Column(name = "static_QR")
    private String staticQr;

    @Column(name = "dynamic_QR")
    private String dynamicQr;

    @Column(name = "logoPath")
    private String logoPath;

    @Column(name = "partners_type")
    private String partnersType;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "logo_type")
    private String logoType;

    @Column(name = "logo_typewhitelabel")
    private String logoTypeWhiteLabel;

    @Column(name = "onboard_completed")
    private String onboardCompleted;

    @Column(name = "split_settlement")
    private Boolean splitSettlement = false;

    @Column(name = "statuschange_on")
    private LocalDateTime statusChangeOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "merchant_type")
    private MerchantType merchantType;

    @Column(name = "parent_merchant_id")
    private String parentMerchantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reseller_type")
    private ResellerType resellerType;

    @Column(name = "is_Partial_OP_enable")
    private String isPartialOpEnable;

    @Column(name = "activation_date")
    private LocalDateTime activationDate;

    @Column(name = "surcharge_label")
    private String surchargeLabel;

    @Column(name = "is_frm")
    private boolean frmFlag = false;

    @Column(name = "merchant_bankname")
    private String merchantBankname;

    @Column(name = "is_demographic")
    private String isDemographic;

    @Column(name = "merchant_group_id")
    private Long merchantGroupMst;

    @Column(name = "is_parent_merchant")
    private boolean isParentMerchant;

    @Column(name = "pay_page_timer")
    private String payPageTimer;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "gst_status")
    private String gstStatus;

    @Column(name = "is_same_business_address")
    private boolean isSameBusinessAddress = false;
}
