package com.betting.security.password_restoring.phone;

import com.betting.security.auth.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class PhonePasswordRestoringController {
    private final PhonePasswordRestoringService passwordRestoringService;
    @GetMapping("/forgot-password/phone")
    public ResponseEntity<String> generatePhoneNumberCode(@RequestParam String phoneNumber) {
        return passwordRestoringService.generatePhoneNumberCode(phoneNumber);
    }
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyPhoneNumberCode(@RequestBody CodeVerificationRequest request) {
        return passwordRestoringService.verifyPhoneNumberCode(request);
    }
    @PostMapping("/change-password/phone")
    public ResponseEntity<AuthenticationResponse> change(@RequestBody PasswordChangingRequest request) {
        return ResponseEntity.ok(passwordRestoringService.changePassword(request));
    }
}
