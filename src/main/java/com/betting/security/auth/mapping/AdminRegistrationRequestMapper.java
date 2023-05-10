package com.betting.security.auth.mapping;

import com.betting.security.auth.admin.AdminAuthenticationRequest;
import com.betting.user.admin.Admin;
import org.springframework.stereotype.Component;

@Component("adminRegistrationMapper")
public class AdminRegistrationRequestMapper implements ObjectMapper<AdminAuthenticationRequest, Admin> {
    @Override
    public Admin mapFrom(AdminAuthenticationRequest request) {
        return Admin.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .secretCode(request.getSecretCode())
                .isEnabled(false)
                .isNonLocked(false)
                .build();
    }
}
