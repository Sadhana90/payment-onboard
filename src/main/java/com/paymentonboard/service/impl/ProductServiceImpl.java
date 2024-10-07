package com.paymentonboard.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymentonboard.dto.OrganizationMasterDTO;
import com.paymentonboard.dto.Theme;
import com.paymentonboard.entity.OrganizationMaster;
import com.paymentonboard.entity.ProductCategory;
import com.paymentonboard.entity.ProductSubCategory;
import com.paymentonboard.repository.OrganizationRepository;
import com.paymentonboard.repository.ProductCategoryRepository;
import com.paymentonboard.repository.ProductSubCategoryRepository;
import com.paymentonboard.service.OrganizationService;
import com.paymentonboard.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	@Autowired
	private ProductSubCategoryRepository  productSubCategoryRepository;

	/*@Override
	public List<ProductCategoryMasterDTO> getProductCategoryData() {
		// TODO Auto-generated method stub
		log.info("Get product category::::::::::");
		// TODO Auto-generated method stub
		List<Object[]> list= productCategoryRepository.findProductDetails(true);
		List<ProductCategoryMasterDTO> productCategoryMasterDTO= new ArrayList();
		for (Object[] obj : list) {
			ProductCategoryMasterDTO categoryMasterDTO = new ProductCategoryMasterDTO();
			categoryMasterDTO.setProductcategory((String) obj[0]);
			ProductSubCategoryMasterDTO subCategoryMasterDTO = new ProductSubCategoryMasterDTO(); 
			subCategoryMasterDTO.setProductsubcategory((String) obj[1]);
			subCategoryMasterDTO.setIcon((String) obj[2]);
			subCategoryMasterDTO.setDescription((String) obj[3]);
			categoryMasterDTO.setProductSubCategoryMasterDTOs(subCategoryMasterDTO);
			productCategoryMasterDTO.add(categoryMasterDTO);		}
		return productCategoryMasterDTO;
	} */
	
	public JSONArray getProductCategoryData() {
        List<ProductCategory> menus = productCategoryRepository.findAll();
      
        JSONArray menuArray = new JSONArray();

        for (ProductCategory menu : menus) {
            JSONObject menuObject = new JSONObject();
            menuObject.put("Id", menu.getId());
            menuObject.put("ProductCategory", menu.getOrgName());

            // Fetch submenus for this menu
            List<ProductSubCategory> subMenus = productSubCategoryRepository.findByproductid(menu.getId());

            JSONArray subMenuArray = new JSONArray();
            for (ProductSubCategory subMenu : subMenus) {
                JSONObject subMenuObject = new JSONObject();
                subMenuObject.put("Id", subMenu.getId());
                subMenuObject.put("SubCategry", subMenu.getProductSubCategory());
                subMenuObject.put("icon", subMenu.getIcon());
                subMenuObject.put("description", subMenu.getDescription());

                subMenuArray.put(subMenuObject);
            }

            menuObject.put("ProductSubCategry", subMenuArray); 
            menuArray.put(menuObject);
        }
        log.info("menus::::::::::"+menuArray);
        return menuArray;
    }
}