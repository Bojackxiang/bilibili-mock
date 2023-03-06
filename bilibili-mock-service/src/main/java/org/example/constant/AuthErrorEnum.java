package org.example.constant;

import org.springframework.stereotype.Component;


public enum AuthErrorEnum {
    // auth error enum code and message
    AUTH_ERROR_INVALID_PHONE("1", "INVALID PHONE NUMBER"),
    AUTH_ERROR_USER_ALREADY_EXISTED("2", "USER ALREADY EXISTS"),
    AUTH_ERROR_UNABLE_DECRYPT_PASSWORD("3", "UNABLE TO DECRYPT PASSWORD"),
    AUTH_ERROR_USER_NOT_EXISTED("4", "USER NOT EXISTS"),
    AUTH_ERROR_INVALID_PASSWORD("5", "INCORRECT PASSWORD"),
    AUTH_ERROR_UNABLE_TO_GET_ALGORITHM("6", "CANNOT GET ALGORITHM"),
    AUTH_ERROR_TOKEN_EXPIRE("7", "TOKEN IS EXPIRED"),
    AUTH_ERROR_TOKEN_VERIFY("8", "VERIFICATION CODE IS EXPIRED"),
    AUTH_ERROR_USER_INFO_NOT_EXISTED("9", "USER INFO NOT EXISTS"),;


    private String code;
    private String message;

    AuthErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
