package com.betting.security.auth.registration;

import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.EmailAlreadyTakenException;
import com.betting.mapping.RegistrationRequestMapper;
import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.security.auth.mail.EmailService;
import com.betting.security.auth.mail.MailType;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.security.auth.validation.TokenValidationService;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PlayerService playerService;
    private final EmailService emailService;
    private final ResponseBuilder responseBuilder;
    private final ConfirmationTokenService confirmationTokenService;
    private final TokenValidationService tokenValidationService;
    public AuthenticationResponse register(RegistrationRequest request, RegistrationRequestMapper mapper) throws MessagingException {
        Player player = mapper.mapFrom(request);
        ThrowableUtils.trueOrElseThrow(s -> s.loadPlayerByUsername(request.getUsername()).isEmpty(), playerService,
                new EmailAlreadyTakenException("email is already taken"));
        String confirmationToken = playerService.signPlayerUp(player);
        String link = "http://localhost:8080/user/confirm?token=" + confirmationToken;
        emailService.sendEmail(request.getUsername(), request.getFullName(), link, MailType.REGISTRATION_CONFIRMATION);
        return responseBuilder.buildResponse(confirmationToken);
    }

    public AuthenticationResponse confirm(String token) {
        // retrieving confirmation token from database
        ConfirmationToken confirmationToken = tokenValidationService.getValidConfirmationToken(token);
        // enabling player to make bets
        confirmationTokenService.updateConfirmed(confirmationToken.getToken());
        Player player = confirmationToken.getPlayer();
        playerService.enablePlayer(player);
        return responseBuilder.buildAuthenticationResponse(player);
    }
}
