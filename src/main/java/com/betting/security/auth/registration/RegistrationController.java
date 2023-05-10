package com.betting.security.auth.registration;

import com.betting.security.auth.mapping.RegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final BeanFactory beanFactory;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerPlayer(@RequestBody RegistrationRequest request) throws MessagingException {
        // TODO: validate registration via email message or via phone number messaging
        // TODO: handle MessagingException
        RegistrationRequestMapper mapper = beanFactory.getBean(RegistrationRequestMapper.class);
        return ResponseEntity.ok(registrationService.register(request, new RegistrationRequestMapper()));
    }

    @GetMapping("/confirm")
    public ResponseEntity<AuthenticationResponse> confirmRegistration(@RequestParam String token) {
        return ResponseEntity.ok(registrationService.confirm(token));
    }
}
