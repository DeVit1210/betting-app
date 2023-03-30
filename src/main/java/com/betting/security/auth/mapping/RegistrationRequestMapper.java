package com.betting.security.auth.mapping;

import com.betting.security.auth.registration.RegistrationRequest;
import com.betting.user.AppUser;
import com.betting.user.player.Player;

public class RegistrationRequestMapper implements ObjectMapper<RegistrationRequest, AppUser> {
    @Override
    public Player mapFrom(RegistrationRequest request) {
        return Player.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .passwordSeries(request.getPassportSeries())
                .passwordNumber(request.getPassportNumber())
                .phoneNumber(request.getPhoneNumber())
                .isEnabled(false)
                .isNonLocked(false)
                .build();
    }
}
