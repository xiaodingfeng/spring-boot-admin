package com.ciicgat.circlefkbff.common.sdk.lang.exception;


import com.ciicgat.circlefkbff.common.sdk.lang.convert.ErrorCenter;
import com.ciicgat.circlefkbff.common.sdk.lang.convert.ErrorCode;
import feign.FeignException;

public class BusinessFeignException extends FeignException implements ErrorCode {

    private static final long serialVersionUID = 1L;

    private final int errorCode;

    private final String errorMsg;


    private ErrorCode error;


    public BusinessFeignException(int status, int errorCode, String errorMsg) {
        super(status, "");
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public final int getErrorCode() {
        return errorCode;
    }

    public final String getErrorMsg() {
        return errorMsg;
    }


    @SuppressWarnings("unchecked")
    public <T extends ErrorCode> T getError() {
        if (error == null) {
            error = ErrorCenter.valueOf(errorCode);
        }
        return (T) error;
    }

    @Override
    public String toString() {
        return "BusinessFeignException{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", error=" + error +
                '}';
    }
}
