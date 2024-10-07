package com.paymentonboard.repository;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.entity.OrganizationMaster;
import com.paymentonboard.entity.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {


	/*@Query(value= "SELECT \r\n" + 
			"pcm.productcategory, psm.productsubcategory, psm.icon, psm.description FROM product_category_mst pcm\r\n" + 
			"INNER JOIN \r\n" + 
			"    product_subcategory_mst psm ON psm.productid = pcm.id \r\n" + 
			"WHERE \r\n" + 
			"    pcm.isactive= :isActive", nativeQuery = true)
	public List<Object[]> findProductDetails(
			@Param("isActive") boolean isActive); */


}