package com.ciicgat.circlefkbff.feign.api;

import com.ciicgat.circlefkbff.feign.Constants;
import com.ciicgat.circlefkbff.feign.FeignApi;
import com.ciicgat.circlefkbff.feign.entity.AdminRole;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.adminrole.AdminRoleAddRequest;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignApi(serviceUrl = Constants.apiUrl)
public interface FeignAdminRoleService {

    @RequestLine("POST /adminrole/grantRole")
    @Headers("Content-Type: application/json")
    Boolean grantRole(@RequestBody AdminRoleAddRequest adminRoleAddRequest);

    @RequestLine("POST /adminrole/cancelGrantRole")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean cancelGrantRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    @RequestLine("POST /adminrole/getAdminRolesByRoleId")
    @Headers("Content-Type: application/x-www-form-urlencoded")
     Page<AdminRole> getAdminRolesByRoleId(
            @Param("roleId")Long roleId,
            @Param("page")Integer page,
            @Param("rowsPerPage")Integer rowsPerPage);

    @RequestLine("POST /adminrole/deleteById")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean deleteById(@Param("id")Long id);

    @RequestLine("POST /adminrole/getAdminRolesByAdminId")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<AdminRole> getAdminRolesByAdminId(@Param("adminId")Long adminId);

    @RequestLine("POST /adminrole/getAdminRoleById")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    AdminRole getAdminRoleById(@Param("id")Long id);

    @RequestLine("POST /adminrole/findAdminRoleByTimeValidToAndRoleType")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<AdminRole> findAdminRoleByTimeValidToAndRoleType(@Param("timeValidTo")String timeValidTo, @Param("roleType")Integer roleType);

    @RequestLine("POST /adminrole/findAdminRoleByTimeValidTo")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    List<AdminRole> findAdminRoleByTimeValidTo(@Param("timeValidTo")String timeValidTo);

    @RequestLine("POST /adminrole/extendAdminRoleValidTime")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean extendAdminRoleValidTime(@Param("id")Long id, @Param("timeValidTo")String timeValidTo);

    @RequestLine("POST /adminrole/getAdminRoleByAdminIdAndRoleId")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    AdminRole getAdminRoleByAdminIdAndRoleId(@Param("adminId")Long adminId, @Param("roleId")Long roleId);

    @RequestLine("POST /adminrole/changeRoleTypeAndTimeValidToById")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Boolean changeRoleTypeAndTimeValidToById(@Param("id")Long id, @Param("roleType")Integer roleType);
}
