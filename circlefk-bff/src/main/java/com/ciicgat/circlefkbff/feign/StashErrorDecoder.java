package com.ciicgat.circlefkbff.feign;

import com.ciicgat.circlefkbff.common.sdk.lang.exception.BusinessRuntimeException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class StashErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return new BusinessRuntimeException(
                    response.status(),
                    response.reason());
    }
}
