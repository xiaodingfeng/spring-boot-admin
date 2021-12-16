package com.ciicgat.circlefk.web.controller.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ciicgat.circlefk.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;
import com.ciicgat.circlefk.common.sdk.lang.exception.Errors;
import com.ciicgat.circlefk.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefk.domain.entity.Admin;
import com.ciicgat.circlefk.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminUpdate;
import com.ciicgat.circlefk.service.auth.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @ApiOperation("搜索管理员信息")
    @PostMapping("/getAdminList")
    public ApiResponse<Page<Admin>> getAdminList(@RequestBody AdminListRequest adminListRequest) {
        System.out.println(JSON.toJSONString(adminListRequest));
        return ApiResponse.success(adminService.getAdminList(adminListRequest));
    }

    @ApiOperation("修改管理员")
    @PostMapping(value = {"/updateAdmin", "/updateAdminInfo"})
    public ApiResponse<Boolean> updateAdmin(@Valid @RequestBody AdminUpdate adminUpdate) {
        return ApiResponse.success(adminService.updateAdmin(adminUpdate));
    }

    @ApiOperation("新增用户")
    @PostMapping("/add")
    public ApiResponse<Long> addEmployee(@Valid @RequestBody AdminAddRequest adminAddRequest) {
        return ApiResponse.success(adminService.add(adminAddRequest));
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

    @ApiOperation("检查原密码后重置密码")
    @PostMapping(value = {"/checkAndResetPassword"})
    public ApiResponse<Boolean> checkAndResetPassword(@RequestParam(value = "adminId") Long adminId,
                                                      @RequestParam(value = "password") String password,
                                                      @RequestParam(value = "newPassword") String newPassword) {
        // 检查原密码
        Boolean result = adminService.checkPassword(adminId, password);
        if (!result) {
            throw new BusinessRuntimeException(Errors.PASSWORD_NOT_MATCH);
        }
        return ApiResponse.success(adminService.resetPassword(adminId, newPassword));
    }

    @ApiOperation("检查密码")
    @PostMapping(value = {"/checkPassword"})
    public ApiResponse<Boolean> checkPassword(@RequestParam(value = "adminId") Long adminId,
                                                      @RequestParam(value = "password") String password) {
        return ApiResponse.success(adminService.checkPassword(adminId, password));
    }

    @ApiOperation("获取用户列表")
    @PostMapping(value = {"/getList"})
    public ApiResponse<List<Admin>> getList(@RequestBody Admin admin) {
        return ApiResponse.success(adminService.getList(admin));
    }

}
