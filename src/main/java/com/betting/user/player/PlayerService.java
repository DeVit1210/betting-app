package com.betting.user.player;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    public Optional<Player> loadPlayerByUsername(String username) {
        return playerRepository.findPlayerByUsername(username);
    }

    public String signPlayerUp(Player player) {
        boolean isPlayerExists = playerRepository.findPlayerByUsername(player.getUsername()).isPresent();
        if(isPlayerExists) {
            // TODO: create exception handlers and make it clear with logging
            throw new IllegalStateException("email is already taken");
        }
        String encodedPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(encodedPassword);
        playerRepository.save(player);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), player
        );
        confirmationTokenService.saveToken(confirmationToken);
        return token;
    }

    public void enablePlayer(String username) {
        playerRepository.enablePlayer(username);
        playerRepository.unlockPlayer(username);
    }

    public Optional<Player> findPlayerByPhoneNumber(String phoneNumber) {
        return playerRepository.findPlayerByPhoneNumber(phoneNumber);
    }

    public void updatePassword(String username, String newPassword) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        playerRepository.updatePassword(username, encodedNewPassword);
    }
}
