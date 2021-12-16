package com.ciicgat.circlefk.common.sdk.lang.exception;

import com.ciicgat.circlefk.common.sdk.lang.convert.ErrorCode;

public class BusinessRuntimeException extends RuntimeException implements ErrorCode {

    private static final long serialVersionUID = 1L;

    private final int errorCode;

    private final String errorMsg;

    public BusinessRuntimeException(int errorCode, String errorMsg) {
        super(errorCode + "");
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessRuntimeException(ErrorCode errorCode) {
        this(errorCode.getErrorCode(), errorCode.getErrorMsg());
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String toString() {
        return "BusinessRuntimeException occurred, errorCode=" + errorCode + ",errorMsg=" + errorMsg;
    }

}
