package com.betting.security.exceptions;

import com.betting.events.exception.EntityNotFoundException;
import com.betting.events.exception.InvalidRequestParameterException;
import com.betting.security.auth.responses.ResponseBuilder;
import com.ctc.wstx.osgi.WstxBundleActivator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {EmailAlreadyTakenException.class})
    public ResponseEntity<?> handleEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return generateResponse(HttpStatus.BAD_REQUEST, e);
    }
    @ExceptionHandler(value = {InvalidConfirmationTokenException.class})
    public ResponseEntity<?> handleInvalidConfirmationTokenException(InvalidConfirmationTokenException e) {
        return generateResponse(HttpStatus.BAD_REQUEST, e);
    }
    @ExceptionHandler(value = {InvalidPhoneCodeException.class})
    public ResponseEntity<?> handleInvalidPhoneCodeException(InvalidPhoneCodeException e) {
        return generateResponse(HttpStatus.BAD_REQUEST, e);
    }
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return generateResponse(HttpStatus.NOT_FOUND, e);
    }
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return generateResponse(HttpStatus.NOT_FOUND, e);
    }
    @ExceptionHandler(value = {InvalidRequestParameterException.class})
    public ResponseEntity<?> handleInvalidRequestParameterException(InvalidRequestParameterException e) {
        return generateResponse(HttpStatus.BAD_REQUEST, e);
    }
    private ResponseEntity<?> generateResponse(HttpStatus status, RuntimeException e) {
        return new ResponseEntity<>(buildApiException(status, e), status);
    }
    private ApiException buildApiException(HttpStatus httpStatus, RuntimeException e) {
        return ApiException.builder()
                .message(e.getMessage())
                .httpStatus(httpStatus)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
