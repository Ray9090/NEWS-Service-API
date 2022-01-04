package com.example.userservice.exception;

public class InvalidJwtAuthenticationException extends FunctionalException {

    public InvalidJwtAuthenticationException() {
        super(FunctionalErrorCode.INVALID_JWT_TOKEN);
    }
}
