package com.betting.security.password_restoring.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsSender smsSender;

    public void sendSMS(String to, String message) {
        smsSender.sendSMS(to, message);
    }
}
