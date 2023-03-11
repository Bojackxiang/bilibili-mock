package org.example.constant;


public enum GroupErrorEnum {
    // auth error enum code and message
    GROUP_ERROR_INVALID_GROUP_ID("1", "INVALID GROUP NAME"),
    GROUP_ERROR_NO_FOLLOWING("2", "USER SUBSCRIBES TO NO ONE"),
    GROUP_ERROR_NO_FANS("3", "USER HAS NO FANS");


    private String code;
    private String message;

    GroupErrorEnum(String code, String message) {
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
