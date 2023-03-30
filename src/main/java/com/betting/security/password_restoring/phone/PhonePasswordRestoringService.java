package com.betting.security.password_restoring.phone;

import com.betting.security.auth.validation.CodeValidationService;
import com.betting.security.exceptions.UserNotFoundException;
import com.betting.security.password_restoring.phone_code.PhoneNumberCode;
import com.betting.security.password_restoring.phone_code.PhoneNumberCodeService;
import com.betting.security.password_restoring.sms.SmsService;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhonePasswordRestoringService {
    private final PlayerService playerService;
    private final PhoneNumberCodeService phoneNumberCodeService;
    private final SmsService smsService;
    private final ResponseBuilder responseBuilder;
    private final CodeValidationService codeValidationService;
    public ResponseEntity<String> generatePhoneNumberCode(String phoneNumber) {
        String correctNumber = "+" + phoneNumber;
        Optional<Player> player = playerService.findPlayerByPhoneNumber(correctNumber);
        if(player.isEmpty()) {
            return ResponseEntity.badRequest().body("there is no player with this phone number");
        }
        String code = phoneNumberCodeService.generateCode(player.get());
        smsService.sendSMS(phoneNumber, "Here is your activation code: " + code);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> verifyPhoneNumberCode(CodeVerificationRequest request) {
        String code = request.getVerificationCode();
        PhoneNumberCode phoneNumberCode = codeValidationService.getValidPhoneCode(code, request.getPhoneNumber());
        phoneNumberCodeService.verifyNumber(code);
        return ResponseEntity.ok(request.getPhoneNumber());
    }

    public AuthenticationResponse changePassword(PasswordChangingRequest request) {
        Player player = playerService.findPlayerByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new UserNotFoundException("player is not found"));
        playerService.updatePassword(player.getUsername(), request.getNewPassword());
        return responseBuilder.buildAuthenticationResponse(player);
    }
}
