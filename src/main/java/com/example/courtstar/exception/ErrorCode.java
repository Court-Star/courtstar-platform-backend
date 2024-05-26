package com.example.courtstar.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    ERROR_CODE(1000,"have error"),
    ACCOUNT_EXIST(1001,"account already exist"),
    EMAIL_INVALID(1002,"email is invalid"),
    PASSWORD_INVALID(1003,"password must be at least 6 characters"),
    PHONE_INVALID(1004,"phone number is invalid"),
    NOT_FOUND_USER(1005,"user not found"),
    UNAUTHENTICATED(1006,"unauthenticated"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    int code;
    String message;
}
