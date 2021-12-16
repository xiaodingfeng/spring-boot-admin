package com.ciicgat.circlefk.common.sdk.lang.exception;

import com.ciicgat.circlefk.common.sdk.lang.convert.ErrorCode;

public enum Errors implements ErrorCode {
    SYS_UNKNOWN_ERROR( 0, "系统未知错误"),
    SYS_UNAUTHORIZED_ERROR( 1, "请求未授权"),
    ADD_USER_USERNAME_IS_EMPTY(30033, "添加用户用户名不能为空"),
    USER_ADD_FAIL(30027, "添加用户失败"),
    PARAMETER_ERROR(103, "参数错误"),
    NO_PRIVILEGE_ERROR( 20004,"没有权限"),
    ADMIN_NOT_EXIST(20001, "管理员信息不存在"),
    VERIFY_CODE_ERROR( 20005, "验证码错误"),
    USER_PASSWORD_ERROR( 20005, "用户账号或者密码错误"),
    USERNAME_EXIST(30014, "用户名已存在"),
    MOBILE_EXIST(30016, "手机号已存在"),
    PASSWORD_RESET_FAIL(30028, "密码重置失败"),
    PASSWORD_NOT_MATCH(30034, "原密码不正确"),
    ;

    private final int errorCode;
    private final String errorMsg;

    /**
     * @param errorCode
     * @param errorMsg
     */
    Errors(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
