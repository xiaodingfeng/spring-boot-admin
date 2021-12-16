package com.ciicgat.circlefkbff.feign.api;

import com.ciicgat.circlefkbff.feign.Constants;
import com.ciicgat.circlefkbff.feign.FeignApi;
import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminUpdate;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignApi(serviceUrl = Constants.apiUrl)
public interface FeignAdminService {

    @RequestLine("POST /admin/add")
    @Headers("Content-Type: application/json")
    Long add(@RequestBody AdminAddRequest adminAddRequest);

    @RequestLine("POST /admin/getById")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Admin getById(@Param("adminId") Long adminId);

    @RequestLine("POST /admin/updateAdmin")
    @Headers("Content-Type: application/json")
    Boolean updateAdmin(@RequestBody AdminUpdate adminUpdate);

    @RequestLine("POST /admin/resetPassword")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean resetPassword(@Param("adminId") Long adminId, @Param("password") String password);

    @RequestLine("POST /admin/getAdminList")
    @Headers("Content-Type: application/json")
    Page<Admin> getAdminList(@RequestBody AdminListRequest adminListRequest);

    @RequestLine("POST /admin/getAdminListByEnterpriseId")
    @Headers("Content-Type: application/json")
    Page<Admin> getAdminListByEnterpriseId(@RequestBody AdminListRequest adminListRequest);

    @RequestLine("POST /admin/getList")
    @Headers("Content-Type: application/json")
    List<Admin> getList(@RequestBody Admin admin);

    @RequestLine("POST /admin/checkPassword")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean checkPassword(@Param("adminId") Long adminId, @Param("password") String password);

    @RequestLine("POST /admin/checkAndResetPassword")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean checkAndResetPassword(@Param("adminId") Long adminId, @Param("password")  String password, @Param("newPassword")  String newPassword);
}
