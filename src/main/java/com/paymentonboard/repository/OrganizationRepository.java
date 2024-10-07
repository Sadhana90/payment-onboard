package com.paymentonboard.repository;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.entity.OrganizationMaster;

public interface OrganizationRepository extends JpaRepository<OrganizationMaster, Long> {


	@Query(value= "SELECT orgmst.*, otm.dark_colour, otm.light_colour, otm.primary_colour, otm.secondary_colour, otm.tertiary_colour FROM organization_mst orgmst INNER JOIN organization_theme_mst otm ON otm.org_id = orgmst.id "
			+ "WHERE orgmst.org_name = :org_name and orgmst.is_active= :isActive", nativeQuery = true)
	public List<Object[]> findOrgDetails(@Param("org_name") String org_name,
			@Param("isActive") boolean isActive);

}