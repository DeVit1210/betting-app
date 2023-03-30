package com.betting.security.password_restoring.email;

import com.betting.security.auth.responses.AuthenticationResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class EmailPasswordRestoringController {
    private final EmailPasswordRestoringService passwordRestoringService;
    @PostMapping("/change-password/email")
    public ResponseEntity<AuthenticationResponse> change(@RequestBody EmailPasswordChangingRequest request) {
        return ResponseEntity.ok(passwordRestoringService.changePassword(request));
    }

    @GetMapping("/forgot-password/email")
    public ResponseEntity<AuthenticationResponse> changePasswordViaEmail(@RequestParam String username) throws MessagingException {
        return ResponseEntity.ok(passwordRestoringService.generateConfirmationToken(username));
    }
}
