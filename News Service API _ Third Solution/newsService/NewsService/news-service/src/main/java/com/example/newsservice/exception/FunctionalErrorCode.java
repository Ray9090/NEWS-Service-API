package com.example.newsservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;

/**
 * This enum represents the functional error codes.
 */
@Getter
public enum FunctionalErrorCode {
    USER_NOT_FOUND(2, HttpStatus.NOT_FOUND, "Login inexist : %s"),
    BAD_REQUEST(0, HttpStatus.BAD_REQUEST, "Bad Request"),
    NOT_FOUND_ENTITY_ID(1, HttpStatus.NOT_FOUND, "No record of type %s and with id %s is present in the database"),
    NOT_FOUND_ENTITY_VALUE(1, HttpStatus.NOT_FOUND, "No record of type %s and with value : %s is present in the database"),
    NOT_NULL_FIELD(4, HttpStatus.BAD_REQUEST, "The following field is required: %s"),
    NOT_NULL_FIELDS(6, HttpStatus.BAD_REQUEST, "The following fields are required : %s"),
    NOT_FOUND_ENTITY(1, HttpStatus.NOT_FOUND, "No record of type %s found"),
    NOT_FOUND(1, HttpStatus.NOT_FOUND, "Not found"),
    UPLOAD_ERROR(24, HttpStatus.BAD_REQUEST, "Error while uploading file : %s");



    private final String code;
    private final HttpStatus httpStatus;
    private final String messageTemplate;

    FunctionalErrorCode(int code, HttpStatus httpStatus, String messageTemplate) {
        this.code = format("%03d", code);
        this.httpStatus = httpStatus;
        this.messageTemplate = messageTemplate;
    }
}
