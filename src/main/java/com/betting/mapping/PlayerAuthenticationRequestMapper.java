package com.betting.mapping;

import com.betting.security.auth.player.PlayerAuthenticationRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("playerAuthenticationMapper")
public class PlayerAuthenticationRequestMapper implements ObjectMapper<PlayerAuthenticationRequest, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken mapFrom(PlayerAuthenticationRequest authenticationRequest) {
        return new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );
    }
}
