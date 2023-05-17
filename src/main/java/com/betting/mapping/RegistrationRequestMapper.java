package com.betting.mapping;

import com.betting.security.auth.registration.RegistrationRequest;
import com.betting.user.AppUser;
import com.betting.user.player.Player;
import org.springframework.stereotype.Component;

@Component("playerRegistrationMapper")
public class RegistrationRequestMapper implements ObjectMapper<RegistrationRequest, AppUser> {
    @Override
    public Player mapFrom(RegistrationRequest request) {
        return Player.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .passportSeries(request.getPassportSeries())
                .passportNumber(request.getPassportNumber())
                .phoneNumber(request.getPhoneNumber())
                .isEnabled(false)
                .isNonLocked(false)
                .build();
    }
}
