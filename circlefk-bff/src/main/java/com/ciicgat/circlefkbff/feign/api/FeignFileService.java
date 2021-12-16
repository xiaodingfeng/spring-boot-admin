package com.ciicgat.circlefkbff.feign.api;

import com.ciicgat.circlefkbff.feign.Constants;
import com.ciicgat.circlefkbff.feign.FeignApi;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.io.File;

@FeignApi(serviceUrl = Constants.apiUrl)
public interface FeignFileService {
    @RequestLine("POST /file/upload")
    @Headers("Content-Type: multipart/form-data")
    String upload(@Param("file") File file);
}
