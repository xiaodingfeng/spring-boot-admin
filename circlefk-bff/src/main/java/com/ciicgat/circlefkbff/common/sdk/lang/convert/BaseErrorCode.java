package com.ciicgat.circlefkbff.common.sdk.lang.convert;


import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;

public class BaseErrorCode implements ErrorCode {
    private static final long serialVersionUID = 1L;

    private final int errorCode;

    private final String errorMsg;

    public BaseErrorCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        ErrorCenter.put(this);
    }


    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseErrorCode)) return false;

        BaseErrorCode that = (BaseErrorCode) o;

        if (getErrorCode() != that.getErrorCode()) return false;
        return errorMsg != null ? errorMsg.equals(that.errorMsg) : that.errorMsg == null;
    }

    @Override
    public int hashCode() {
        int result = getErrorCode();
        result = 31 * result + (errorMsg != null ? errorMsg.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
