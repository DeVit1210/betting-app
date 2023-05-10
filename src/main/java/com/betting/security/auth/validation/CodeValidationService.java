package com.betting.security.auth.validation;

import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.InvalidPhoneCodeException;
import com.betting.security.password_restoring.phone_code.PhoneNumberCode;
import com.betting.security.password_restoring.phone_code.PhoneNumberCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.betting.security.auth.validation.CodeValidationResult.*;
import static com.betting.security.auth.validation.PhoneNumberCodeValidator.*;

@Service
@RequiredArgsConstructor
public class CodeValidationService {
    private final PhoneNumberCodeService phoneNumberCodeService;

    public PhoneNumberCode getValidPhoneCode(String code, String phoneNumber) {
        PhoneNumberCode phoneNumberCode = phoneNumberCodeService.findByCode(code);
        CodeValidationResult result = isCodeNotUsed()
                .and(isCodeNonExpired())
                .and(isCodeBelongToTheNumber(phoneNumber))
                .apply(phoneNumberCode);
        ThrowableUtils.trueOrElseThrow(res -> Objects.equals(result, SUCCESS), result, new InvalidPhoneCodeException(result.name()));
        return phoneNumberCode;
    }
}
