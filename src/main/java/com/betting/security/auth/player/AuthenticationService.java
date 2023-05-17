package com.betting.security.auth.player;

import com.betting.mapping.PlayerAuthenticationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final ResponseBuilder authenticationResponseBuilder;
    private final PlayerService playerService;
    public AuthenticationResponse authenticate(PlayerAuthenticationRequest request, PlayerAuthenticationRequestMapper mapper) {
        authenticationManager.authenticate(mapper.mapFrom(request));
        // TODO: check for existence
        Player player = playerService.loadPlayerByUsername(request.getUsername()).get();
        return authenticationResponseBuilder.buildAuthenticationResponse(player);
    }
}
