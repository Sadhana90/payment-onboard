/*
 * package com.paymentonboard.repository;
 * 
 * import java.util.Date; import java.util.List; import java.util.Map; import
 * java.util.Optional;
 * 
 * import javax.transaction.Transactional;
 * 
 * import org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.data.jpa.repository.Modifying; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.data.repository.query.Param;
 * 
 * 
 * public interface MerchantRepository extends JpaRepository<MerchantList, Long>
 * { // @Query(value =
 * "call pro_MerchantList(:fromDate, :toDate, :merchantId, :merchantName);",
 * nativeQuery = true)
 * 
 * @Query(value =
 * "call pro_MerchantListV2(:fromDate, :toDate, :merchantId, :resellerId, :merchantName, :norecord, :pageno, :tradeName, :partnerType);"
 * , nativeQuery = true) List<Object[]> findByMerchantsByDateNameId(String
 * fromDate, String toDate, String merchantId, String resellerId, String
 * merchantName , int norecord, int pageno,String tradeName,String partnerType);
 * 
 * 
 * @Query(value = "call pro_GetMerchantId();", nativeQuery = true) String
 * findMerchantId();
 * 
 * 
 * @Transactional
 * 
 * @Query(value =
 * "call pro_MerchantCreation(:contactPerson, :contactNumber, :emailId, :companypan, :merchantname, :businessname, :businesstype, :DateofIncroporation, :merchantcatagorycode, :merchantsubcatagory, :businessmodal,"
 * +
 * " :turnoverfyear, :monthlyincome, :avgamtpertransaction, :authorisedpan, :nameaspan, :registeraddress, :pincode, :city, :State, :gstno, :website, :testaccess, :instruments, :merchantid,:transactionkey, :isPanVerified, :iResellerId, :iIsCompanyPanVerify, :iIsGSTVerify, :iCompanyPanVerifyName, :iGSTVerifyName, :name_as_perpan ,:Source, :merReturnUrl, :additional_contact, :sales_lead, :logoPath, :partners_type,:bank_id, :whitelistPath, :logoPathWhitelbeltype, :isplit_settlement, "
 * +
 * ":merchantType, :parentMerchantId, :resellerType, :merchantBankName, :merchantGroupId, :parentMerchantFlag, :businessAddress, :gstStatus, :isSameBusinessAddress);"
 * , nativeQuery = true) List<Object[]> createMerchantByNameEmail(String
 * contactPerson, String contactNumber, String emailId, String companypan,
 * String merchantname, String businessname, int businesstype, String
 * DateofIncroporation, String merchantcatagorycode, String merchantsubcatagory,
 * String businessmodal, String turnoverfyear, String monthlyincome, String
 * avgamtpertransaction, String authorisedpan, String nameaspan, String
 * registeraddress, String pincode, String city, String State, String gstno,
 * String website, String testaccess, String instruments, String merchantid,
 * String transactionkey, Character isPanVerified, String iResellerId, Character
 * iIsCompanyPanVerify, Character iIsGSTVerify, String iCompanyPanVerifyName,
 * String iGSTVerifyName, String name_as_perpan, String Source, String
 * merReturnUrl , String additional_contact, String sales_lead, String logoPath,
 * String partners_type, String bank_id, String whitelistPath, String
 * logoPathWhitelbeltype, Boolean isplit_settlement, String merchantType, String
 * parentMerchantId, String resellerType, String merchantBankName, String
 * merchantGroupId, boolean parentMerchantFlag, String businessAddress, String
 * gstStatus, boolean isSameBusinessAddress);// 28 values + 1 more as
 * requirement
 * 
 * @Query(value =
 * "call pro_MerchantBasicSetup(:MerchantId, :isAutoRefund, :hours, :minutes, :isPushUrl, :pushUrl, :settlementCycle,"
 * +
 * "	:merchantDashboardRefund, :mdDisableRefundCc, :mdDisableRefundDc, :mdDisableRefundNb, :mdDisableRefundUpi,"
 * +
 * "	:mdDisableRefundWallet, :refundApi, :refundApiDisableCc,:refundApiDisableDc,:refundApiDisableNb, :refundApiDisableUpi, :refundApiDisableWallet,"
 * +
 * " :integrationType, :isretryAllowed, :bpsEmailNotification, :bpsSmsNotification, :bpsMailReminder, :reportingcycle, :upi_loader, :upi_intent, :upi_collect, :static_QR, :dynamic_QR, :pay_page_timer);"
 * , nativeQuery = true)
 * 
 * List<Object[]> cBasicSetupByMerchantId(String MerchantId, String
 * isAutoRefund, String hours, String minutes, String isPushUrl, String pushUrl,
 * String settlementCycle, String merchantDashboardRefund, String
 * mdDisableRefundCc, String mdDisableRefundDc, String mdDisableRefundNb, String
 * mdDisableRefundUpi, String mdDisableRefundWallet, String refundApi, String
 * refundApiDisableCc, String refundApiDisableDc, String refundApiDisableNb,
 * String refundApiDisableUpi, String refundApiDisableWallet, String
 * integrationType, String isretryAllowed, String bpsEmailNotification, String
 * bpsSmsNotification, String bpsMailReminder, String reportingcycle, String
 * upi_loader, String upi_intent, String upi_collect,String static_QR,String
 * dynamic_QR, String pay_page_timer);//24 value
 * 
 * 
 * @Query(value = "call pro_MerchantBasicSetupDetails(:MerchantId);",
 * nativeQuery = true)
 * 
 * List<Object[]> findBasicSetupByMerchantId(String MerchantId );
 * 
 * @Query(value =
 * "SELECT if(COUNT(*)>0,'true','false') AS my_bool FROM tbl_mstmerchant where contact_number=? ;"
 * , nativeQuery = true) boolean findByContactNumber(String contact_number);
 * 
 * @Query(value =
 * "SELECT if(COUNT(*)>0,'true','false') AS my_bool FROM tbl_mstmerchant where email_id=? ;"
 * , nativeQuery = true) boolean findByEmailId(String email_id);
 * 
 * @Query(value =
 * "SELECT if(COUNT(*)>0,'true','false') AS my_bool FROM tbl_mstmerchant where companypan=? ;"
 * , nativeQuery = true) boolean findByCompanyPAN(String companyPAN);
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value = "INSERT INTO tbl_mstmerchant\r\n" + "(MerchantId,\r\n" +
 * "contact_person,\r\n"
 * 
 * + "email_id,\r\n" + "contact_number,\r\n" + "status,\r\n" + "created_on,\r\n"
 * + "max_token_size,\r\n" +
 * "merchant_type\r\n) values (:merchantId, :fullName, :emailId, :contactNo, :status, :strDate, :token, 'DIRECT_DVP_MERCHANT')"
 * , nativeQuery = true) void createMerchantSelf(String merchantId, String
 * fullName, String emailId, String contactNo, String status, Date strDate,
 * String token);
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "UPDATE tbl_selfonboard set verified = :verfication where otp_num_Mobile= :otp"
 * , nativeQuery = true) void updateOTPverified(@Param ("otp") String
 * otp, @Param ("verfication") String verfication );
 * 
 * @Modifying
 * 
 * @Transactional
 * 
 * @Query(value = "SET SQL_SAFE_UPDATES = 0;", nativeQuery = true) void
 * setSafeMode();
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "UPDATE tbl_merchantbulkuploadcsv set remarks = :isverifiy, upload_status = :uploadStatus where Id= :recId"
 * , nativeQuery = true) void updateVerification(@Param ("isverifiy") String
 * isverifiy , @Param ("uploadStatus") String uploadStatus , @Param ("recId")
 * Long recId );
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value = "UPDATE tbl_merchantbulkuploadcsv set remarks = :isverifiy",
 * nativeQuery = true) void updateAllVerification(@Param ("isverifiy") String
 * isverifiy);
 * 
 * 
 * @Query(value =
 * "select remarks from tbl_merchantbulkuploadcsv where Id= :recId", nativeQuery
 * = true) String findVerification(Long recId );
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value = "delete from tbl_instrumentactivationstatus where Id=:Id",
 * nativeQuery = true) void deleteInstrumentList(String Id);
 * 
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value = "UPDATE tbl_mdrbulkuploadcsv set remarks = :isverifiy",
 * nativeQuery = true) void updateMdrVerification(@Param ("isverifiy") String
 * isverifiy);
 * 
 * @Query(value = "select * from tbl_merchantbulkuploadcsv", nativeQuery = true)
 * List<Object[]> listAllRecords ();
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "UPDATE tbl_mstmerchant set email_cred_triggered = :emailTrigger where MerchantId= :mid"
 * , nativeQuery = true) void updateEmailTriggerFlag(@Param ("emailTrigger")
 * String emailTrigger, @Param ("mid") String mid );
 * 
 * public Optional<MerchantList> getMerchantListByMerchantId(String merchantId);
 * 
 * @Query(value =
 * "SELECT if(COUNT(*)>0,'true','false') AS USERID FROM tbl_mstuser where USERID = :validuserId"
 * , nativeQuery = true) boolean findByuserId(String validuserId);
 * 
 * @Query(value =
 * "select brand_name from tbl_reseller_personal_details where reseller_id= :validuserId"
 * , nativeQuery = true) String findUserIdByRId(String validuserId);
 * 
 * @Query(value =
 * "select business_name from tbl_mstmerchant where MerchantId= :validuserId",
 * nativeQuery = true) String findUserIdByMId(String validuserId);
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "UPDATE tbl_mstuser set password = :passwordChange where USERID= :UserId",
 * nativeQuery = true) void updateResetPass(@Param ("UserId") String
 * UserId, @Param ("passwordChange") String passwordChange );
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "UPDATE tbl_forgetpass_trail set verified = :verfication where uu_id= :tokenValidation"
 * , nativeQuery = true) void updateverified(@Param ("tokenValidation") String
 * tokenValidation, @Param ("verfication") String verfication );
 * 
 * @Query(value =
 * "SELECT * FROM tbl_forgetpass_trail where uu_id = :tokenValidation",
 * nativeQuery = true) List<Object[]> findByuuId(String tokenValidation);
 * 
 * @Query(value =
 * "SELECT USERID, FullName, EmailId  FROM tbl_mstuser where USERID= :validuserId and Is_Deleted='N'"
 * , nativeQuery = true) List<Object[]> findUserIdById(String validuserId);
 * 
 * @Query(value = "SELECT password FROM tbl_mstuser where USERID = :userId",
 * nativeQuery = true) String findUserIdByPass(String userId);
 * 
 * @Query(value =
 * "SELECT sp_name FROM tbl_mstserviceprovider where sp_id=:spid ;", nativeQuery
 * = true) String findspName(int spid);
 * 
 * @Query(value =
 * "SELECT InstrumentDescription FROM tbl_mstpayment_instruments where InstrumentId=:instrumentId ;"
 * , nativeQuery = true) String findInstrumentName(String instrumentId);
 * 
 * @Query(value = "Select bankName from tbl_mstpgbank where bankId=:bankId ;",
 * nativeQuery = true) String findBankName(String bankId);
 * 
 * 
 * @Query(value =
 * "call pro_GetRefundApplyListFiltersV2(:merchantId, :fromDate, :toDate, :id, :bankId, :custMobile, :custMail,"
 * + "	 :txnId, :spId);", nativeQuery = true)
 * 
 * List<Map<String, Object>> refundAmtList(String merchantId, String fromDate,
 * String toDate, String id, String bankId, String custMobile, String custMail,
 * String txnId, String spId);
 * 
 * 
 * @Query(value = "select * from tbl_mstmerchant where MerchantId= :MerchantId",
 * nativeQuery = true) List<Object[]> findByMerchant(String MerchantId );
 * 
 * @Query(value =
 * "SELECT merchant_id FROM tbl_transactionmaster where txn_id= :txnId",
 * nativeQuery = true) String findtransctionById(String txnId);
 * 
 * @Query(value = "SELECT txn_id FROM tbl_transactionmaster where Id= :txnId",
 * nativeQuery = true) String findtransctionBytxnId(String txnId);
 * 
 * @Query(value =
 * "select merchant_id,txn_Id from tbl_transactionmaster where Id= :txnId",
 * nativeQuery = true) List<Object[]> getTxnDetailsById(long txnId);
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "INSERT INTO bulk_refund_batch_details (batch_id, merchant_id, transaction_count, refund_amount, status, is_deleted, file_path) values (:batch_id, :merchant_id, :transaction_count, :refund_amount, :status, :is_deleted, :file_path)"
 * , nativeQuery = true) void updateTableRecords(String batch_id, String
 * merchant_id, Integer transaction_count, Double refund_amount, String status,
 * Integer is_deleted, String file_path);
 * 
 * @Query(value =
 * "SELECT recon_status FROM tbl_transactionmaster where txn_id= :txnId",
 * nativeQuery = true) String findByProperty(String txnId);
 * 
 * // @Query(value =
 * "SELECT Id FROM tbl_transactionmaster where txn_Id=:transId and merchant_id=:merchant_Id ;"
 * , nativeQuery = true) // String fintransction(String transId, String
 * merchant_Id);
 * 
 * @Query(value =
 * "SELECT Id FROM tbl_transactionmaster where txn_Id=:transId ;", nativeQuery =
 * true) String fintransction(String transId);
 * 
 * @Query(value =
 * "SELECT process_id FROM tbl_transactionmaster where txn_Id=:transId ;",
 * nativeQuery = true) String fintransctionProcessId(String transId);
 * 
 * @Query(value =
 * "select serviceProvider.refund_processor from tbl_mstserviceprovider as serviceProvider where sp_id =:processId and isRefundAPI = 'Y'"
 * , nativeQuery = true) String finRefundProcessor(String processId);
 * 
 * @Query(value =
 * "SELECT merchant_id FROM tbl_transactionmaster where txn_Id=:transId ;",
 * nativeQuery = true) String fintransctionMid(String transId);
 * 
 * List<MerchantList> getByIsDeletedAndResellerId(String isDeleted, String
 * resellerId);
 * 
 * @Query("FROM MerchantList WHERE isDeleted=:isDeleted AND (merchantId like %:merchantId% OR merchantName like %:merchantId%)"
 * ) List<MerchantList> getByIsDeletedAndMerchantIdLike(String isDeleted, String
 * merchantId);
 * 
 * 
 * List<MerchantList> findByStatus(String status);
 * 
 * @Query(value =
 * "select Id  from tbl_mstbusinesstype where BusinessType=:businessType ;",
 * nativeQuery = true) String getbussinestypeId(String businessType);
 * 
 * @Query(value =
 * "select BusinessType  from tbl_mstbusinesstype where Id =:businesstype_Id ;",
 * nativeQuery = true) String getbussinestypevalue(String businesstype_Id);
 * 
 * @Query(value =
 * "Select category_id from tbl_merchant_category where business_desc=:merchant_category_code ;"
 * , nativeQuery = true) String getmerchant_category_codeId(String
 * merchant_category_code);
 * 
 * @Query(value =
 * "select SubCatId from tbl_merchant_subcategory where CategoryId=:merchant_category_code1 and SubCategory=:merchant_sub_category ;"
 * , nativeQuery = true) String getmerchant_sub_categoryId(String
 * merchant_sub_category, String merchant_category_code1);
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(
 * value="Update tbl_merchantbulkuploadcsv set merchantid=:merchantId where Id=:Id1 ;"
 * , nativeQuery = true) void updatemerchantId(String merchantId, int Id1);
 * 
 * @Query(value =
 * "select MerchantId FROM tbl_mstmerchant where reseller_id =:resellerId",
 * nativeQuery = true) List<String> getMerchantIdListByResellerId(String
 * resellerId);
 * 
 * List<MerchantList> findByMerchantTypeAndIsDeleted(MerchantType merchantType,
 * String isDeleted);
 * 
 * MerchantList findByMerchantIdAndIsDeleted(String merchantId, String
 * isDeleted);
 * 
 * List<MerchantList> findByMerchantTypeAndResellerIdAndIsDeleted(MerchantType
 * merchantType, String resellerId, String isDeleted);
 * 
 * 
 * List<MerchantList> findAllByIsDeleted(String isDeleted);
 * 
 * List<MerchantList> findByMerchantGroupMst(Long groupId);
 * 
 * @Query(value =
 * "from MerchantList where merchantGroupMst = :groupId and status != :status")
 * List<MerchantList> findByMerchantGroupMstAndStatusNot(@Param("groupId") Long
 * groupId, @Param("status") String status);
 * 
 * @Query(value =
 * "Select bankName from tbl_mstpgbank where bankId=:bankId and sp_id = :processId  ;"
 * , nativeQuery = true) String findBankNameWithProcessId(@Param("bankId")
 * String bankId, @Param("processId") String processId);
 * 
 * @Transactional
 * 
 * @Modifying
 * 
 * @Query(value =
 * "UPDATE tbl_selfonboard set verified = :verfication where otp_num_email= :otp"
 * , nativeQuery = true) void updateOTPverifiedByEmail(@Param ("otp") String
 * otp, @Param ("verfication") String verfication);
 * 
 * @Query(value =
 * "select shortname from tbl_reseller_personal_details where reseller_id= :validuserId"
 * , nativeQuery = true) String findShortNameByRId(String validuserId);
 * 
 * @Query(value =
 * "select reseller_id from tbl_mstmerchant where MerchantId= :validuserId",
 * nativeQuery = true) String findResellerIdByMId(String validuserId);
 * 
 * List<MerchantList> findByMerchantId(String merchantId); }
 */