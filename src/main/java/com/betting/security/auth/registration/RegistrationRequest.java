package com.betting.security.auth.registration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String username;
    private String password;
    private String phoneNumber;
    private String passportSeries;
    private String passportNumber;
    private String fullName;

    // TODO: adjust fields quantity to access proper player registration
}
