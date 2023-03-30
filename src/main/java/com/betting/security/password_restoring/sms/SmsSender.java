package com.betting.security.password_restoring.sms;

import org.springframework.stereotype.Component;

@Component
public interface SmsSender {
    void sendSMS(String to, String message);
}
