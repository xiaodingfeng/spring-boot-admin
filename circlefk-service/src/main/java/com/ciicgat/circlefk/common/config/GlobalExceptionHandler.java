package com.ciicgat.circlefk.common.config;

import com.ciicgat.circlefk.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefk.common.sdk.lang.convert.ErrorCode;
import com.ciicgat.circlefk.common.sdk.lang.convert.StandardErrorCode;
import com.ciicgat.circlefk.common.sdk.lang.convert.WebProperties;
import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final WebProperties webProperties;

    public GlobalExceptionHandler(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    /**
     * 一般是当前系统业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessRuntimeException.class)
    public ApiResponse handleBusinessRuntimeException(BusinessRuntimeException e) {
        if (webProperties.isPrintBusinessErrorStack()) {
            LOGGER.warn(e.toString(), e);
        } else {
            LOGGER.warn("BusinessRuntimeException, errorCode {} errorMsg {}", e.getErrorCode(), e.getErrorMsg());
        }
        return ApiResponse.fail(e);
    }


    /**
     * 一般ServletException都是http请求方式不对引起的异常，可以认为是参数错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler({
            ServletException.class,
            MissingServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            NoHandlerFoundException.class})
    public ApiResponse handleServletException(ServletException e) {
        LOGGER.warn("error", e);
        String errorMsg = "请求格式不正确";
        if (e.getClass() == MissingServletRequestParameterException.class) {
            errorMsg = "请求参数缺失";
        }

        return ApiResponse.fail(new StandardErrorCode(webProperties.getErrorCodePrefix(), 2, 0, errorMsg));
    }


    /**
     * 一般的，TypeMismatchException也可以认为是参数错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler({
            TypeMismatchException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentConversionNotSupportedException.class})
    public ApiResponse handleTypeMismatchException(TypeMismatchException e) {
        LOGGER.warn("TypeMismatchException, error: " + e.getMessage());
        return ApiResponse.fail(new StandardErrorCode(webProperties.getErrorCodePrefix(), 2, 0, "前端参数错误"));
    }

    @ExceptionHandler(BindException.class)
    public ApiResponse handleBindException(BindException e) {
        LOGGER.warn("BindException, param validate error: " + e.getMessage());
        return handleBindingResult(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.warn("MethodArgumentNotValidException, param validate error: " + e.getMessage());
        return handleBindingResult(e.getBindingResult());
    }

    private ApiResponse handleBindingResult(BindingResult bindingResult) {
        String errorMsg = "前端参数错误";
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                if (error instanceof FieldError && webProperties.isShowFieldNameInError()) {
                    stringBuilder.append(((FieldError) error).getField()).append(" ").append(error.getDefaultMessage()).append(" ");
                } else {
                    stringBuilder.append(error.getDefaultMessage()).append(" ");
                }
            }
            if (stringBuilder.length() > 1) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            errorMsg = stringBuilder.toString();
        }
        return ApiResponse.fail(new StandardErrorCode(webProperties.getErrorCodePrefix(), 2, 0, errorMsg));
    }


    /**
     * 一般的，ConstraintViolationException都是参数错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse handleValidationException(ConstraintViolationException e) {
        LOGGER.warn("ConstraintViolationException param validate error: " + e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder stringBuilder = new StringBuilder();
        for (ConstraintViolation<?> item : violations) {
            stringBuilder.append(item.getMessage()).append(" ");
        }
        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return ApiResponse.fail(new StandardErrorCode(webProperties.getErrorCodePrefix(), 2, 0, stringBuilder.toString()));
    }


    @ExceptionHandler
    public ApiResponse<Object> handleThrowable(HttpServletRequest request, Throwable throwable) {
        //为了应用迁移方便
        if (throwable instanceof ErrorCode) {
            LOGGER.warn("{}, errorCode {} errorMsg {}", throwable.getClass().getSimpleName(), ((ErrorCode) throwable).getErrorCode(), ((ErrorCode) throwable).getErrorMsg());
            return ApiResponse.fail((ErrorCode) throwable);
        }

        StringBuffer requestInfo = request.getRequestURL();
        requestInfo.append("，params=>");
        request.getParameterMap().forEach((key, value) -> requestInfo.append(key).append(":").append(Arrays.toString(value)));
        String msg = "发生异常，请求信息为：" + requestInfo.toString();
        // 是否要发送邮件通知
        LOGGER.error(msg, throwable);
        String errorMsg = "系统异常";
        if (throwable instanceof SQLException) {
            errorMsg = "数据库异常";
        }
        return ApiResponse.fail(new StandardErrorCode(webProperties.getErrorCodePrefix(), 1, 0, errorMsg));
    }
}
