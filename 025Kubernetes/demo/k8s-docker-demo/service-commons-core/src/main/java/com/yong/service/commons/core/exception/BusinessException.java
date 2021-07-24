package com.yong.service.commons.core.exception;

import com.yong.service.commons.core.data.ResultCode;

public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException() {
        this.code = ResultCode.ERROR.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();;
    }
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
