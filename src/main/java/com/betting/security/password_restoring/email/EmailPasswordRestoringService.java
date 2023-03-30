package com.betting.security.password_restoring.email;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.security.auth.mail.EmailService;
import com.betting.security.auth.mail.MailType;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.security.auth.validation.TokenValidationService;
import com.betting.security.exceptions.UserNotFoundException;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailPasswordRestoringService {
    private final TokenValidationService tokenValidationService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResponseBuilder responseBuilder;
    private final PlayerService playerService;
    private final EmailService emailService;
    public AuthenticationResponse changePassword(EmailPasswordChangingRequest request) {
        ConfirmationToken confirmationToken = tokenValidationService.getValidConfirmationToken(request.getConfirmationToken());
        // enabling player to make bets
        confirmationTokenService.updateConfirmed(confirmationToken.getToken());
        Player player = confirmationToken.getPlayer();
        playerService.updatePassword(player.getUsername(), request.getNewPassword());
        return responseBuilder.buildAuthenticationResponse(player);
    }

    public AuthenticationResponse generateConfirmationToken(String username) throws MessagingException {
        Player player = playerService.loadPlayerByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("player is not found"));
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), player
        );
        String link = "http://localhost:8080/user/forgot-password/confirm?token=" + token;
        emailService.sendEmail(username, player.getFullName(), link, MailType.PASSWORD_CHANGING);
        confirmationTokenService.saveToken(confirmationToken);
        return responseBuilder.buildResponse(token);
    }
}
