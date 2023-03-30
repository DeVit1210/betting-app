package com.betting.security.auth.admin;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AdminAuthenticationRequest {
    private String username;
    private String password;
    private String secretCode;
}
