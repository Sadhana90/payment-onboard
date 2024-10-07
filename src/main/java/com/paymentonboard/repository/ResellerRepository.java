package com.paymentonboard.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.paymentonboard.entity.SelfOnboard;

public interface ResellerRepository extends JpaRepository<SelfOnboard, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO tbl_selfonboard\r\n"
			+ "(full_name,\r\n"
			+ "email_id,\r\n"
			
			+ "contact_no,\r\n"
			+ "otp_num_mobile,\r\n"
			+ "otp_num_email,\r\n"
			+ "otp_retry_count) values (:fullName, :emailId, :mobileNo, :sOtp, :eOtp, :retryCount)", nativeQuery = true)
	void insertSetailsSelfonBoard(String fullName, String emailId, String mobileNo, String sOtp, String eOtp,Integer retryCount);
	
	@Query(value="select id, otp_retry_count from tbl_selfonboard where email_Id=:emailId and contact_no=:mobileNo order by id desc limit 1;", nativeQuery = true)
	List<Object[]> findLastOTP(String emailId, String mobileNo);
	
	@Query(value="SELECT (COUNT(*) > 0) AS emailOtpvalid FROM tbl_selfonboard WHERE otp_num_email = :getEmailOTP", nativeQuery = true)
	boolean findbyEOtp(String getEmailOTP);
	
	@Query(value="SELECT (COUNT(*) > 0) AS emailvalid FROM tbl_selfonboard where email_id=:emailId and verified='1'", nativeQuery = true)
	boolean findUserByEmail(String emailId);
	
	@Query(value="SELECT * FROM tbl_selfonboard where otp_num_email=:getEmailOTP", nativeQuery = true)
	List<Object[]> findbyEmailOtp(String getEmailOTP);
}

