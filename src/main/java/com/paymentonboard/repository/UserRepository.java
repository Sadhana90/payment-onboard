package com.paymentonboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.paymentonboard.entity.User;


public interface UserRepository extends JpaRepository<User, String> {

	@Query(value = "select case when isnull(logoPath) then'' else logoPath end as logoPath, case when isnull(logo_typeWhitelabel) then '' else logo_typeWhitelabel end as logo_typeWhitelabel from tbl_mstmerchant where MerchantId = ?", nativeQuery = true)
	List<Object[]> findVerificationLogo(String Username);
	
	@Query(value = "select case when isnull(logoPathWhitelabel) then '' else logoPathWhitelabel end as logoPathWhitelabel, case when isnull(logo_typeWhitelabel) then '' else logo_typeWhitelabel end as logo_typeWhitelabel from tbl_reseller_personal_details where reseller_id = ?", nativeQuery = true)
	List<Object[]> findaggregatorLogo(String username);
	
	@Query(value = "select ROLEID from tbl_mstuser where userid= iuid and IS_ACTIVE='1' and Is_Deleted='N' and blocked='0'", nativeQuery = true)
	Integer getRoleIdByUserId(String iuid);

	Optional<User> findByUserId(String userId);
	
	List<User> findByMerchantId(String merchantId);

	@Query(value = "SELECT mstuser.USERID, mstuser.ContactNo, mstuser.EmailId, mstuser.FullName "
			+ "FROM tbl_mstuser mstuser " + "INNER JOIN user_merchant_mapping usermerchant "
			+ "ON mstuser.USERID = usermerchant.userid "
			+ "WHERE usermerchant.is_active = '1' and usermerchant.merchant_id =:merchantId and mstuser.USERID !=:merchantId", nativeQuery = true)
	List<Object[]> findUserListByMerchantId(String merchantId);

    boolean existsByUserId(String userId);

	@Query(value = "SELECT id, MerchantId,email_id,merchant_name ,contact_number FROM tbl_mstmerchant WHERE merchant_group_id = ( SELECT merchant_group_id FROM tbl_mstmerchant WHERE MerchantId = ?1)", nativeQuery = true)
	List<Object[]> findUsersMerchant(String merchantId);


	@Query(value = "SELECT mstuser.USERID, mstuser.FullName"
			+ " FROM tbl_mstuser mstuser INNER JOIN user_merchant_mapping usermerchant"
			+ " ON mstuser.USERID = usermerchant.merchant_id"
			+ " WHERE usermerchant.userid = ? and usermerchant.is_active = true", nativeQuery = true)
	List<Object[]> findMerchantListByUserId(String userId);
	
	@Query(value = "SELECT mstuser.USERID, mstuser.FullName"
			+ " FROM tbl_mstuser mstuser"
			+ " WHERE mstuser.USERID = (Select MerchantId from tbl_mstuser where USERID = ?)", nativeQuery = true)
	List<Object[]> findMainMerchantByUserId(String userId);

	List<User> findByMerchantIdAndUserIdIsNotOrderByCreatedOnDesc(String merchantId, String merchantId2);
	
	@Modifying
	@Query(value = "UPDATE tbl_mstuser SET Is_Deleted=1, IS_ACTIVE=0 WHERE USERID = :userId", nativeQuery = true)
	void deleteUserByUserId(String userId);


}
