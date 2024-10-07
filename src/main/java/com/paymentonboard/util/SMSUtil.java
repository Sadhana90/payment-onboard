package com.paymentonboard.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSUtil {

    @Autowired
    NotificationProperty notificationProperty;

    public String generateOTP(boolean defaultOTP) {
        return defaultOTP ? notificationProperty.getDefaultOtp() : RandomStringUtils.randomNumeric(notificationProperty.getOtpLength());
    }
}
