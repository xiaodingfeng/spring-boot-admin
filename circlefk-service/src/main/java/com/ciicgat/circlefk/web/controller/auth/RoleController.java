package com.ciicgat.circlefk.web.controller.auth;

import com.ciicgat.circlefk.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefk.domain.entity.Role;
import com.ciicgat.circlefk.protocol.request.role.RoleAdd;
import com.ciicgat.circlefk.protocol.request.role.RoleEdit;
import com.ciicgat.circlefk.service.auth.RoleService;
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
    public ApiResponse<Role> getRoleById(@RequestParam(value = "id") Long id) {
//        throw  new BusinessRuntimeException(-1,"哈哈哈哈或或");
        return ApiResponse.success(roleService.get(id));
    }

    @RequestMapping(name = "根据企业id获取角色列表所有信息", value = "/getRoleListByEnterpriseId", method = RequestMethod.POST)
    public ApiResponse<List<Role>> getRoleListByEnterpriseId(@RequestParam(value = "enterpriseId") Long enterpriseId) {
        return ApiResponse.success(roleService.getRoleListByEnterpriseId(enterpriseId));
    }

    @RequestMapping(name = "根据roleId，删除角色", value = "/deleteRoleById", method = RequestMethod.POST)
    public ApiResponse<Boolean> deleteRoleById(@RequestParam(value = "id") long id) {
        return ApiResponse.success(roleService.deleteRoleById(id));
    }

    @RequestMapping(name = "根据roleId，修改角色信息", value = "/updateRoleInfo", method = RequestMethod.POST)
    public ApiResponse<Integer> updateRoleInfo(@Valid @RequestBody RoleEdit roleEdit) {
        return ApiResponse.success(roleService.updateRoleInfo(roleEdit));
    }

    @RequestMapping(name = "新增角色", value = "/addRoleInfo", method = RequestMethod.POST)
    public ApiResponse<Long> addRoleInfo(@Valid @RequestBody RoleAdd roleAdd) {
        return ApiResponse.success(roleService.addRoleInfo(roleAdd));
    }
}
