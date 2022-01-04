package com.example.userservice.exception;


import static com.example.userservice.exception.FunctionalErrorCode.EMAIL_ALREADY_EXISTE_EXCEPTION;

public class EmailAlreadyExisteException extends FunctionalException {

    private static final long serialVersionUID = 4896893580503414663L;

    public EmailAlreadyExisteException() {
        super(EMAIL_ALREADY_EXISTE_EXCEPTION);
    }


}
