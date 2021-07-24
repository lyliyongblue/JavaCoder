package com.yong.service.commons.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yong.service.commons.core.exception.BusinessException;

import static com.yong.service.commons.core.data.ResultCode.ERROR;
import static com.yong.service.commons.core.data.ResultCode.SUCCESS;

public class ResultHandler<T> {

    private String code;
    private String message;
    private T data;
    public ResultHandler() {
        this(null);
    }

    public ResultHandler(T data) {
        this(SUCCESS.getCode(), SUCCESS.getMessage(), data);
    }

    public ResultHandler(String code, String message) {
        this(code, message, null);
    }

    public ResultHandler(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T>ResultHandler<T> success(T data) {
        return new ResultHandler<>(data);
    }

    public static <T>ResultHandler<T> getInstance(String code, String message, T data) {
        return new ResultHandler<>(code, message, data);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS.getCode().equals(code);
    }

    public static <T>T getResultData(ResultHandler<T> resultHandler) {
        if(resultHandler == null || resultHandler.getData() == null) {
            throw new BusinessException("数据为空");
        }
        if(!resultHandler.isSuccess()) {
            throw new BusinessException(resultHandler.getCode(), resultHandler.getMessage());
        }
        return resultHandler.getData();
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
