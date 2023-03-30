package com.betting.security.password_restoring.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("twilio")
public class NexmoSmsSender implements SmsSender {
    @Value("${nexmo.api.key}")
    private String apiKey;

    @Value("${nexmo.api.secret}")
    private String apiSecret;

    @Override
    public void sendSMS(String to, String message) {
        VonageClient client = VonageClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();
        com.vonage.client.sms.messages.TextMessage textMessage =
                new TextMessage("Vonage APIs", "48699525169", "text");
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}
