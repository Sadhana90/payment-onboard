package com.paymentonboard.util;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GeneralUtil {


    @Value("${key}")
    String key;

    @Value("${initVector}")
    String initVector;


    public String crypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            log.error("GeneralUtil.java ::: ::: Getting error :: {0}", ex);
        }

        return null;
    }
    
    public static String isUserMatching(String authString) {
        String Statusapp = null;
        if (authString.equals("4ae7df22936b7be31fd9ca5d99c6614a")) {
            Statusapp = "Admin";
        } else if (authString.equals("af9d1620e1bd350400ca30a793782d21")) {
            Statusapp = "Merchant";
        } else if (authString.equals("b2b799c9e6a4557990afc253ec54e521")) {
            Statusapp = "Aggregator";
        }


        return Statusapp;
    }
}
