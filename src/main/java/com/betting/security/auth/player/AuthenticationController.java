package com.betting.security.auth.player;

import com.betting.security.auth.mapping.PlayerAuthenticationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody PlayerAuthenticationRequest request) {
        // TODO: work through other codes of response
        return ResponseEntity.ok(authenticationService.authenticate(request, new PlayerAuthenticationRequestMapper()));
    }
}