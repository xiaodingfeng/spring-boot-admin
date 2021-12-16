package com.ciicgat.circlefkbff.web.controller.auth;

import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.feign.entity.Role;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.role.RoleAddRequest;
import com.ciicgat.circlefkbff.protocol.request.role.RoleEditRequest;
import com.ciicgat.circlefkbff.protocol.response.auth.AdminRoleList;
import com.ciicgat.circlefkbff.service.auth.RoleService;
import com.ciicgat.circlefkbff.web.annotation.Auth;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping(name = "根据角色id获取角色信息", value = "/getRoleById", method = RequestMethod.POST)
    @Auth("role_select")
    public ApiResponse<Role> getRoleById(@RequestParam(value = "id") Long id) {
        return ApiResponse.success(roleService.get(id));
    }

    @RequestMapping(name = "根据企业id获取角色列表所有信息", value = "/getRoleListByEnterpriseId", method = RequestMethod.POST)
    @Auth("role_select")
    public ApiResponse<List<Role>> getRoleListByEnterpriseId(@RequestParam(value = "enterpriseId") Long enterpriseId) {
        return ApiResponse.success(roleService.getRoleListByEnterpriseId(enterpriseId));
    }

    @RequestMapping(path = "/getAdminRolesByRoleId", method = {RequestMethod.POST})
    @ApiOperation(value = "根据roleId获取当前角色信息", notes = "根据roleId获取当前角色信息")
    public ApiResponse<Page<AdminRoleList>> getAdminRolesByRoleId(
            @RequestParam(value = "roleId") long roleId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rowsPerPage", required = false, defaultValue = "10") Integer rowsPerPage
    ) {
        // build code
        return ApiResponse.success(roleService.getAdminRolesByRoleId(
                roleId,
                page,
                rowsPerPage
        ));
    }

    @RequestMapping(name = "根据roleId，删除角色", value = "/deleteRoleById", method = RequestMethod.POST)
    public ApiResponse<Boolean> deleteRoleById(@RequestParam(value = "id") long id) {
        return ApiResponse.success(roleService.deleteRoleById(id));
    }

    @RequestMapping(name = "根据roleId，修改角色信息", value = "/updateRoleInfo", method = RequestMethod.POST)
    public ApiResponse<Integer> updateRoleInfo(@Valid @RequestBody RoleEditRequest roleEditRequest) {
        return ApiResponse.success(roleService.updateRoleInfo(roleEditRequest));
    }

    @RequestMapping(name = "新增角色", value = "/addRoleInfo", method = RequestMethod.POST)
    public ApiResponse<Long> addRoleInfo(@Valid @RequestBody RoleAddRequest roleAdd) {
        return ApiResponse.success(roleService.addRoleInfo(roleAdd));
    }
}
