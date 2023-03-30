package com.betting.security.auth.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    public void sendEmail(String to, String name, String link, MailType mailType) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setSubject("Confirm your email");
        helper.setText(buildEmail(mailType, name, link), true);
        helper.setTo(to);
        javaMailSender.send(mimeMessage);
    }

    private String buildEmail(MailType mailType, String name, String link) {
        return switch (mailType) {
            case REGISTRATION_CONFIRMATION -> MailBuilder.buildEmail("Confirm your account", name,
                    "Thank you for registering. Please click on the below link to activate your account: ",
                    link, "Activate now", "The link will expire in 15 minutes"
            );
            case PASSWORD_CHANGING -> MailBuilder.buildEmail("Change your password", name,
                    "Here is a link for you to confirm your intention to change password. Click on it to set a new password",
                    link, "Change now", "The link will expire in 15 minutes");
        };
    }
}
