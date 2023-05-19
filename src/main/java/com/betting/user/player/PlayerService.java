package com.betting.user.player;

import com.betting.exceptions.EntityNotFoundException;
import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.user.player.account.AccountService;
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
    private final AccountService accountService;
    public Optional<Player> loadPlayerByUsername(String username) {
        return playerRepository.findPlayerByUsername(username);
    }

    public String signPlayerUp(Player player) {
        boolean isPlayerExists = playerRepository.findPlayerByUsername(player.getUsername()).isPresent();
        if(isPlayerExists) {
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

    public void enablePlayer(Player player) {
        playerRepository.enablePlayer(player.getUsername());
        playerRepository.unlockPlayer(player.getUsername());
        accountService.bind(player);
    }

    public Optional<Player> findPlayerByPhoneNumber(String phoneNumber) {
        return playerRepository.findPlayerByPhoneNumber(phoneNumber);
    }

    public Player findById(Long playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException(Player.class));
    }

    public void updatePassword(String username, String newPassword) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        playerRepository.updatePassword(username, encodedNewPassword);
    }
}
