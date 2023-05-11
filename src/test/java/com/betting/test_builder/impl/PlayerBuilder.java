package com.betting.test_builder.impl;

import com.betting.test_builder.TestBuilder;
import com.betting.user.player.Player;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor(staticName = "aPlayerBuilder")
@AllArgsConstructor
@With
public class PlayerBuilder implements TestBuilder<Player> {
    private String username = "username";
    private String password = "password";
    private String phoneNumber = "+375331234567";
    private String passportSeries = "AB";
    private String passportNumber = "1234567";
    private String fullName = "Full Name";
    private boolean isNonLocked = false;
    private boolean isEnabled = false;

    @Override
    public Player build() {
        return Player.builder()
                .username(username)
                .password(password)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .passportNumber(passportNumber)
                .passportSeries(passportSeries)
                .isNonLocked(isNonLocked)
                .isEnabled(isEnabled)
                .build();
    }
}
