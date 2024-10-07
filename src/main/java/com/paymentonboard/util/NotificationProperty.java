package com.paymentonboard.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class NotificationProperty {

    @Value("${SMSConfig.otpLength}")
    Integer otpLength;

    @Value("${SMSConfig.defaultOtp}")
    String defaultOtp;

}
