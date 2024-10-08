package com.paymentonboard.service;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.paymentonboard.dto.UserRequest;

public interface CommonService {

    public String GetMerchantList(String Name) throws Exception;

    public String uploadForm(File file);

    public ArrayList getDropDown(String Type, String Value) throws SQLException;

    public UserRequest userLoginDetails(UserRequest loginRequestDTO) throws Exception;

    public String getMenuHierarchy(String roleId) throws Exception;

}
