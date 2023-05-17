package com.betting.security.auth.player;

import com.betting.mapping.PlayerAuthenticationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final BeanFactory beanFactory;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody PlayerAuthenticationRequest request) {
        // TODO: work through other codes of response
        PlayerAuthenticationRequestMapper mapper = beanFactory.getBean(PlayerAuthenticationRequestMapper.class);
        return ResponseEntity.ok(authenticationService.authenticate(request, mapper));
    }
}