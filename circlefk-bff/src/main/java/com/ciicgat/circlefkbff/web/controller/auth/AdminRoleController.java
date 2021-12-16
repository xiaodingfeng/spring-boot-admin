package com.ciicgat.circlefkbff.web.controller.auth;

import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.feign.entity.AdminRole;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.adminrole.AdminRoleAddRequest;
import com.ciicgat.circlefkbff.service.auth.AdminRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/adminrole")
public class AdminRoleController {
    @Resource
    private AdminRoleService adminRoleService;

    @ApiOperation("给员工授权某个角色")
    @PostMapping("/grantRole")
    public ApiResponse<Boolean> grantRole(@Valid @RequestBody AdminRoleAddRequest adminRoleAddRequest) {
        return ApiResponse.success(adminRoleService.grantRole(adminRoleAddRequest));
    }

    @ApiOperation("取消员工某个角色的授权")
    @PostMapping("/cancelGrantRole")
    public ApiResponse<Boolean> cancelGrantRole(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "adminId") Long adminId

    ) {
        return ApiResponse.success(adminRoleService.cancelGrantRole(roleId, adminId));
    }

    @ApiOperation("根据roleId,获取当前已授权信息")
    @PostMapping("/getAdminRolesByRoleId")
    public ApiResponse<Page<AdminRole>> getAdminRolesByRoleId(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rowsPerPage", required = false, defaultValue = "10") Integer rowsPerPage
    ) {
        return ApiResponse.success(adminRoleService.getAdminRolesByRoleId(
                roleId,
                page,
                rowsPerPage
        ));
    }

    @ApiOperation("删除角色对员工的授权")
    @PostMapping("/deleteById")
    public ApiResponse<Boolean> deleteById(
            @RequestParam(value = "id") Long id
    ) {
        return ApiResponse.success(adminRoleService.deleteById(id));
    }

    @ApiOperation("获取员工当前拥有的所有权限")
    @PostMapping("/getAdminRolesByAdminId")
    public ApiResponse<List<AdminRole>> getAdminRolesByAdminId(
            @RequestParam(value = "adminId") Long adminId
    ) {
        return ApiResponse.success(adminRoleService.getAdminRolesByAdminId(adminId));
    }

    @ApiOperation("根据Id获取单个已授权用户记录")
    @PostMapping("/getAdminRoleById")
    public ApiResponse<AdminRole> getAdminRoleById(
            @RequestParam(value = "id") Long id
    ) {
        return ApiResponse.success(adminRoleService.getAdminRoleById(id));
    }

    @ApiOperation("给某个角色延迟有效期")
    @PostMapping("/extendAdminRoleValidTime")
    public ApiResponse<Boolean> extendAdminRoleValidTime(
            @RequestParam(value = "id")  Long id,
            @RequestParam(value = "timeValidTo")  String timeValidTo
    ) {
        return ApiResponse.success(adminRoleService.extendAdminRoleValidTime(id, timeValidTo));
    }

    @ApiOperation("根据adminId,角色Id获取单个已授权角色")
    @PostMapping("/getAdminRoleByAdminIdAndRoleId")
    public ApiResponse<AdminRole> getAdminRoleByAdminIdAndRoleId(
            @RequestParam(value = "adminId")  Long adminId,
            @RequestParam(value = "roleId")  Long roleId
    ) {
        return ApiResponse.success(adminRoleService.getAdminRoleByAdminIdAndRoleId(adminId, roleId));
    }

    @ApiOperation("根据id修改角色类型和过期时间")
    @PostMapping("/changeRoleTypeAndTimeValidToById")
    public ApiResponse<Boolean> changeRoleTypeAndTimeValidToById(
            @RequestParam(value = "id")  Long id,
            @RequestParam(value = "roleType")  Integer roleType
    ) {
        return ApiResponse.success(adminRoleService.changeRoleTypeAndTimeValidToById(id, roleType));
    }
}
