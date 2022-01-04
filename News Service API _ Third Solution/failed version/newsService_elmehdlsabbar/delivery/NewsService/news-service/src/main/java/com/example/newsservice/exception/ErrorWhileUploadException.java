package com.example.newsservice.exception;


import static com.example.newsservice.exception.FunctionalErrorCode.UPLOAD_ERROR;

public class ErrorWhileUploadException extends FunctionalException {

    private static final long serialVersionUID = 1L;

    public ErrorWhileUploadException(String value) {
        super(UPLOAD_ERROR,value);
    }


}
