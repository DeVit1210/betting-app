package com.betting.security.auth.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerAuthenticationRequest {
    private String username;
    private String password;
}
