package com.betting.security.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
public class ApiException {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timeStamp;
}
