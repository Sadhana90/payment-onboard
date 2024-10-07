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
import com.paymentonboard.entity.ProductSubCategory;

@Repository
public interface ProductSubCategoryRepository extends JpaRepository<ProductSubCategory, Long> {

	List<ProductSubCategory> findByproductid(Long productid);
}