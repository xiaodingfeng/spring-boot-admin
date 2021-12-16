package com.ciicgat.circlefkbff.feign.api;

import com.ciicgat.circlefkbff.feign.Constants;
import com.ciicgat.circlefkbff.feign.FeignApi;
import com.ciicgat.circlefkbff.feign.entity.Role;
import com.ciicgat.circlefkbff.protocol.request.role.RoleAdd;
import com.ciicgat.circlefkbff.protocol.request.role.RoleEdit;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignApi(serviceUrl = Constants.apiUrl)
public interface FeignRoleService {

    @RequestLine("POST /role/addRoleInfo")
    @Headers("Content-Type: application/json")
    Long addRoleInfo(@RequestBody RoleAdd roleAdd);

    @RequestLine("POST /role/updateRoleInfo")
    @Headers("Content-Type: application/json")
    Integer updateRoleInfo(@RequestBody RoleEdit roleEdit);

    @RequestLine("POST /role/deleteRoleById")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean deleteRoleById(@Param("id") Long id);

    @RequestLine("POST /role/getRoleListByEnterpriseId")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<Role> getRoleListByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @RequestLine("POST /role/getRoleById")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Role get(@Param("id") Long id);

//    @RequestLine("GET /search?q={q}")
//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    ApiResponse<Role> get(@Param("q") Long q);
}
