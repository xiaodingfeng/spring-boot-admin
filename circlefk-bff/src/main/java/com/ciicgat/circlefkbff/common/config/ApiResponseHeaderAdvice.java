package com.ciicgat.circlefkbff.common.config;

import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.common.sdk.lang.convert.ErrorCode;
import com.ciicgat.circlefkbff.common.sdk.lang.url.UrlCoder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 将ApiResponse的code、msg加到返回的header中
 * Created by August.Zhou on 2019-05-28 19:05.
 */
@ControllerAdvice
public class ApiResponseHeaderAdvice implements ResponseBodyAdvice<Object> {

    private static final String V1 = "1.0";
    private static final String V2 = "2.0";

    private static final String ERROR_CODE_HEADER = "x-error-code";
    private static final String ERROR_MSG_HEADER = "x-error-msg";
    private static final String API_VERSION_HEADER = "x-api-version";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ApiResponse) {
            ApiResponse apiResponse = (ApiResponse) body;
            attachHeader(response, apiResponse.getCode(), apiResponse.getMsg(), V1);

            return body;
        } else if (body instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) body;
            if (responseEntity.getBody() instanceof ApiResponse) {
                ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
                attachHeader(response, apiResponse.getCode(), apiResponse.getMsg(), V1);
                return body;
            }
        }

        attachHeader(response, ErrorCode.SUCCESS.getErrorCode(), ErrorCode.SUCCESS.getErrorMsg(), V2);
        return body;
    }

    private void attachHeader(ServerHttpResponse response, int errorCode, String errorMsg, String version) {
        response.getHeaders().add(ERROR_CODE_HEADER, String.valueOf(errorCode));
        response.getHeaders().add(ERROR_MSG_HEADER, errorMsg == null ? "" : UrlCoder.encode(errorMsg));
        response.getHeaders().add(API_VERSION_HEADER, version);
    }
}
