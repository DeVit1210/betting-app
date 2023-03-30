package com.betting.security.password_restoring.email;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmailPasswordChangingRequest {
    private String confirmationToken;
    private String newPassword;
}
