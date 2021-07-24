package com.yong.service.commons.core.data;

public enum ResultCode {
    SUCCESS("200", "success"),
    ERROR("500", "error");

    private final String code;
    private final String message;
    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
