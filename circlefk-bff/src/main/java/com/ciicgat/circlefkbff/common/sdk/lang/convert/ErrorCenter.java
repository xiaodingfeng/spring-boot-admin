package com.ciicgat.circlefkbff.common.sdk.lang.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ErrorCenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCenter.class);


    private static final Map<Integer, ErrorCode> ALL_ERROR_CODES = new TreeMap<>();

    public static void put(ErrorCode errorCode) {
        Objects.requireNonNull(errorCode);
        ALL_ERROR_CODES.put(errorCode.getErrorCode(), errorCode);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ErrorCode> T valueOf(Integer code) {
        T value = (T) ALL_ERROR_CODES.get(code);
        if (value == null) {
            LOGGER.warn("return null,code {}", code);
        }
        return value;
    }

    /**
     * 此方法用于注册相关的errorcode到ErrorCenter里。
     * 这样子，返回的BusinessFeignException.getError就能返回正确的枚举对象，业务可以通过枚举==来判断。
     * 避免使用int值来判断。
     * @param classes
     */
    public static void initErrorCodeEnum(Class<? extends Enum>... classes) {
        for (Class aClass : classes) {
            if (ErrorCode.class.isAssignableFrom(aClass) && !ErrorCode.class.equals(aClass) && aClass.isEnum()) {
                ErrorCode[] value = (ErrorCode[]) aClass.getEnumConstants();
                for (ErrorCode e : value) {
                    put(e);
                }
            }
        }
    }
}

