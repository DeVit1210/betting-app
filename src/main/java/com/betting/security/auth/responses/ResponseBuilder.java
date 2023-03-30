package com.betting.security.auth.responses;

import com.betting.security.jwt.JwtUtils;
import com.betting.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ResponseBuilder {
    private final JwtUtils jwtUtils;
    private Random random = new Random();
    public AuthenticationResponse buildAuthenticationResponse(AppUser appUser) {
        String jwtToken = jwtUtils.generateToken(Collections.singletonMap("password", appUser.getPassword()), appUser);
        return buildResponse(jwtToken);
    }

    public AuthenticationResponse buildAuthenticationResponse(AppUser appUser, Map<String, Object> claims) {
        String jwtToken = jwtUtils.generateToken(claims, appUser);
        return buildResponse(jwtToken);
    }

    public AuthenticationResponse buildResponse(String token) {
        return AuthenticationResponse.builder().authenticationToken(token).build();
    }
}
