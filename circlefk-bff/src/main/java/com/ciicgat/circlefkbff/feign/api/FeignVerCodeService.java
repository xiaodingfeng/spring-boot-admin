package com.ciicgat.circlefkbff.feign.api;

import com.ciicgat.circlefkbff.feign.Constants;
import com.ciicgat.circlefkbff.feign.FeignApi;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@FeignApi(serviceUrl = Constants.apiUrl)
public interface FeignVerCodeService {

    @RequestLine("POST /vercode/createCaptcha")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    String createCaptcha(@Param("sessionId") String sessionId, @Param("length") Integer length);

    @RequestLine("POST /vercode/getVerCode")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    String getVerCode(@Param("sessionId") Long sessionId);

    @RequestLine("POST /vercode/validateToken")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean validateTokenRedis(@Param("token") String token);

    @RequestLine("POST /vercode/addToken")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean addToken(@Param("token") String token, @Param("s") Long s);
}
