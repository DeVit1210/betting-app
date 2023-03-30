package com.betting.security.password_restoring.phone;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PasswordChangingRequest {
    private String phoneNumber;
    private String newPassword;
}
