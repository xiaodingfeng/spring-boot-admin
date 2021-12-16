package com.ciicgat.circlefkbff.web.controller.auth;

import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminUpdate;
import com.ciicgat.circlefkbff.service.auth.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @ApiOperation("搜索管理员信息")
    @PostMapping("/getAdminList")
    public ApiResponse<Page<Admin>> getAdminList(@RequestBody AdminListRequest adminListRequest) {
        return ApiResponse.success(adminService.getAdminList(adminListRequest));
    }

    @ApiOperation("查看管理员")
    @PostMapping("/getById")
    public ApiResponse<Admin> getById(@RequestParam(value = "adminId") Long adminId) {
        return ApiResponse.success(adminService.getById(adminId));
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public ApiResponse<Boolean> resetPassword(@RequestParam(value = "adminId") Long adminId,
                                      @RequestParam(value = "password") String password) {
        return ApiResponse.success(adminService.resetPassword(adminId, password));
    }

    @ApiOperation("查一个企业所有用户")
    @PostMapping("/getAdminListByEnterpriseId")
    public ApiResponse<Page<Admin>> getAdminListByEnterpriseId(@RequestBody AdminListRequest adminListRequest) {
        return ApiResponse.success(adminService.getAdminListByEnterpriseId(adminListRequest));
    }

    @ApiOperation("修改管理员")
    @PostMapping("/updateAdmin")
    public ApiResponse<Boolean> updateAdmin(@Valid @RequestBody AdminUpdate adminUpdate) {
        return ApiResponse.success(adminService.updateAdmin(adminUpdate));
    }

    @ApiOperation("新增用户")
    @PostMapping("/add")
    public ApiResponse<Long> addEmployee(@Valid @RequestBody AdminAddRequest adminAddRequest) {
        return ApiResponse.success(adminService.add(adminAddRequest));
    }

    @ApiOperation("检查原密码后重置密码")
    @PostMapping(value = {"/checkAndResetPassword"})
    public ApiResponse<Boolean> checkAndResetPassword(@RequestParam(value = "adminId") Long adminId,
                                                      @RequestParam(value = "password") String password,
                                                      @RequestParam(value = "newPassword") String newPassword) {
        return ApiResponse.success(adminService.checkAndResetPassword(adminId, password, newPassword));
    }
}
