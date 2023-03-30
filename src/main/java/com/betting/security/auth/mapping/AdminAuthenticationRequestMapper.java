package com.betting.security.auth.mapping;


import com.betting.security.auth.admin.AdminAuthenticationRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AdminAuthenticationRequestMapper implements ObjectMapper<AdminAuthenticationRequest, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken mapFrom(AdminAuthenticationRequest request) {
        return new UsernamePasswordAuthenticationToken(
                request,
                request.getSecretCode()
        );
    }
}
