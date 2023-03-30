package com.betting.security.auth.mapping;

import com.betting.security.auth.player.PlayerAuthenticationRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PlayerAuthenticationRequestMapper implements ObjectMapper<PlayerAuthenticationRequest, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken mapFrom(PlayerAuthenticationRequest authenticationRequest) {
        return new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );
    }
}
