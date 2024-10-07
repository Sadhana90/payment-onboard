package com.paymentonboard.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import com.paymentonboard.dto.UserRequest;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

/*@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRequest user1;

    @Autowired
    private UserRequest user2;
    @Autowired
    private CommonService CommonService;

    @Autowired
    private JdbcTemplate JdbcTemplate;


    private static final String MERCHANT_ROLE_ID = "2";
    private static final String INVOICE_MANAGEMENT_MENU_ID = "112";
    private static final String USER_MANAGEMENT_MENU_ID = "201";
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}



    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            user1.setUsername(username);
            user2 = CommonService.userLoginDetails(user1);

            if (username != null) {
                return new User(username, user2.getPassword(), new ArrayList<>());
            } else {
                throw new UsernameNotFoundException("User not found!!!");
            }
        } catch (Exception e) {
            log.error("Error :: {0}", e);
        }
        return null;

    } */
//} */
