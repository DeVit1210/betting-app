package com.betting.security.auth.admin;

import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.EmailAlreadyTakenException;
import com.betting.mapping.AdminAuthenticationRequestMapper;
import com.betting.mapping.AdminRegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.user.admin.Admin;
import com.betting.user.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final ResponseBuilder responseBuilder;
    public AuthenticationResponse authenticate(AdminAuthenticationRequest request, AdminAuthenticationRequestMapper mapper) {
        authenticationManager.authenticate(mapper.mapFrom(request));
        // TODO: check for existence
        Admin admin = adminRepository.findAdminByUsername(request.getUsername()).get();
        return responseBuilder.buildAuthenticationResponse(admin, generateAdminClaims(request));
    }

    public Map<String, Object> generateAdminClaims(AdminAuthenticationRequest request) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("password", request.getPassword());
        claims.put("secret_code", request.getSecretCode());
        return claims;
    }

    public AuthenticationResponse register(AdminAuthenticationRequest request, AdminRegistrationRequestMapper mapper) {
        Admin admin = mapper.mapFrom(request);
        ThrowableUtils.trueOrElseThrow(r -> r.findAdminByUsername(admin.getUsername()).isEmpty(), adminRepository,
                new EmailAlreadyTakenException("email is already taken"));
        adminRepository.save(admin);
        return responseBuilder.buildAuthenticationResponse(admin, generateAdminClaims(request));
    }
}
