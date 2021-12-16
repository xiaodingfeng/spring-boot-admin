package com.ciicgat.circlefk.web.controller.auth;

import com.ciicgat.circlefk.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefk.domain.entity.Role;
import com.ciicgat.circlefk.protocol.response.auth.AdminRealm;
import com.ciicgat.circlefk.protocol.response.auth.Permission;
import com.ciicgat.circlefk.protocol.response.auth.SimpleAdminRealm;
import com.ciicgat.circlefk.service.auth.RoleService;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/adminrealm")
public class AdminRealmController {

    @Resource
    private RoleService roleService;


    @ApiOperation("根据管理员id获取管理员信息")
    @PostMapping("/getAdminRealm")
    public ApiResponse<AdminRealm> getAdminRealm(@RequestParam(value = "adminId") Long adminId) {
        return ApiResponse.success(roleService.getAdminRealm(adminId));
    }

    @ApiOperation("获取管理员对应应用的操作权限列表")
    @PostMapping("/getAdminOperateRights")
    public ApiResponse<List<String>> getAdminOperateRights(@RequestParam(value = "adminId") Long adminId) {
        List<String> rightList = new ArrayList<>();

        AdminRealm adminRealm = roleService.getAdminRealm(adminId);
        if (Objects.isNull(adminRealm)) {
            return ApiResponse.success(rightList);
        }

        List<Role> roles = adminRealm.getRoles();

        for (Role role : roles) {

            Permission permissionEntity = role.getPermissionEntity();
            if (Objects.isNull(permissionEntity)) {
                continue;
            }

            Map<String, Set<String>> rights = permissionEntity.getRights();
            if (Objects.isNull(rights)) {
                continue;
            }

            rights.forEach((key, value) -> rightList.addAll(value));
        }

        return ApiResponse.success(rightList.stream().distinct().collect(Collectors.toList()));
    }

    @ApiOperation("获取管理员对应应用的操作权限及菜单列表")
    @PostMapping("/getAdminOperateRightsAndMenus")
    public ApiResponse<SimpleAdminRealm> getAdminOperateRightsAndMenus(@RequestParam(value = "adminId") Long adminId) {
        AdminRealm adminRealm = roleService.getAdminRealm(adminId);

        SimpleAdminRealm simpleAdminRealm = new SimpleAdminRealm();
        if (adminRealm != null && adminRealm.getPermissionEntity() != null) {

            simpleAdminRealm.setRightList(Maps.transformEntries(adminRealm.getPermissionEntity().getRights(), (key, val) -> new ArrayList<>(val)));
            simpleAdminRealm.setMenuList(new ArrayList<>(adminRealm.getPermissionEntity().getMenus()));
        }

        return ApiResponse.success(simpleAdminRealm);
    }
}
