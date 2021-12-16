package com.ciicgat.circlefkbff.feign.api;

import com.ciicgat.circlefkbff.feign.Constants;
import com.ciicgat.circlefkbff.feign.FeignApi;
import com.ciicgat.circlefkbff.protocol.response.auth.AdminRealm;
import com.ciicgat.circlefkbff.protocol.response.auth.SimpleAdminRealm;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@FeignApi(serviceUrl = Constants.apiUrl)
public interface FeignAdminRealmService {

    @RequestLine("POST /adminrealm/getAdminRealm")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    AdminRealm getAdminRealm(@Param("adminId") Long adminId);

    @RequestLine("POST /adminrealm/getAdminOperateRights")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<String> getAdminOperateRights(@Param("adminId") Long adminId);

    @RequestLine("POST /adminrealm/getAdminOperateRightsAndMenus")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    SimpleAdminRealm getAdminOperateRightsAndMenus(@Param("adminId") Long adminId);

}
