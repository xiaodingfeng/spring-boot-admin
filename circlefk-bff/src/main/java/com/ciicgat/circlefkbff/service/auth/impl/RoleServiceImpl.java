package com.ciicgat.circlefkbff.service.auth.impl;

import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefkbff.feign.api.FeignRoleService;
import com.ciicgat.circlefkbff.feign.entity.AdminRole;
import com.ciicgat.circlefkbff.feign.entity.Role;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.role.*;
import com.ciicgat.circlefkbff.protocol.response.auth.AdminRoleList;
import com.ciicgat.circlefkbff.service.auth.AdminRoleService;
import com.ciicgat.circlefkbff.service.auth.AdminService;
import com.ciicgat.circlefkbff.service.auth.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Resource
    private FeignRoleService roleService;

    @Resource
    private AdminRoleService adminRoleService;

    @Resource
    private AdminService adminService;

    @Override
    public Long addRoleInfo(RoleAddRequest request) {
        // 遍历对象，拼接新增role所需要的字段
        RoleAdd roleAdd = new RoleAdd();
        roleAdd.setEnterpriseId(request.getEnterpriseId());
        roleAdd.setName(request.getName());
        roleAdd.setDescription(request.getDescription());
        Map<String, String> permissions = this.makePermissionString(request.getPermissionGroup());
        roleAdd.setPermissions(permissions.get("permissions"));
        roleAdd.setPermissionsText(permissions.get("permissionsText"));
        return roleService.addRoleInfo(roleAdd);
    }

    @Override
    public Integer updateRoleInfo(RoleEditRequest roleEditRequest) {
        // 遍历对象，拼接需要更新的字段
        RoleEdit roleEdit = new RoleEdit();
        roleEdit.setId(roleEditRequest.getRoleId());
        roleEdit.setName(roleEditRequest.getName());
        roleEdit.setDescription(roleEditRequest.getDescription());
        Map<String, String> permissions = this.makePermissionString(roleEditRequest.getPermissionGroup());
        roleEdit.setPermissions(permissions.get("permissions"));
        roleEdit.setPermissionsText(permissions.get("permissionsText"));
        return roleService.updateRoleInfo(roleEdit);
    }

    @Override
    public Boolean deleteRoleById(Long id) {
        return null;
    }

    @Override
    public List<Role> getRoleListByEnterpriseId(Long enterpriseId) {
        return roleService.getRoleListByEnterpriseId(enterpriseId);
    }

    @Override
    public Role get(Long id) {
        return roleService.get(id);
    }

    /**
     * 拼接成原php存放的格式
     */
    private Map<String, String> makePermissionString(List<PermissionGroupRequest> permissionGroup) {
        Map<String,Object> returnMap = new HashMap<>(); //最终的结果集
        Map<String,Object> returnTextMap = new HashMap<>(); //最终的结果集,中文

        List<String> menuArray = new LinkedList<>(); // 可见的菜单栏
        List<String> menuTextArray = new LinkedList<>(); // 可见的菜单栏中文
        Map<String, List<String>> rightsMap = new HashMap<>();  // 可见的权限
        Map<String, List<String>> rightsTextMap = new HashMap<>(); // 可见的权限中文
        // 先遍历所有菜单，如果当前菜单栏是可见的，则需要处理
        for (PermissionGroupRequest permissionGroupRequest : permissionGroup) {
            if (permissionGroupRequest.getMenuVisible()) {
                menuArray.add(permissionGroupRequest.getMenuid());
                menuTextArray.add(permissionGroupRequest.getMenuname());
            }

            // 再遍历该菜单下面所有权限，如果当前权限是选中的，则需要处理
            List<PermissionRequest> permissions = permissionGroupRequest.getPermissions();
            List<String> rightArray = new LinkedList<>();  // 选中的权限
            List<String> rightTextArray = new LinkedList<>(); // 选中的权限中文
            boolean flag = false;
            for (PermissionRequest permission : permissions) {
                if (permission.getChecked()) {
                    rightArray.add(permission.getPermissionid());
                    rightTextArray.add(permission.getPermissionname());
                    flag = true;
                }
            }
            if (flag) {
                rightsMap.put(permissionGroupRequest.getMenuid(), rightArray);
                rightsTextMap.put(permissionGroupRequest.getMenuname(), rightTextArray);
            }
        }
        returnMap.put("rights", rightsMap);
        returnMap.put("menus", menuArray);


        returnTextMap.put("rights", rightsTextMap);
        returnTextMap.put("menus", menuTextArray);


        // 开始对returnMap和returnTextMap做php的序列化处理

        Map<String, String> returnArray = new HashMap<>();
        returnArray.put("permissions", JSON.toJSONString(returnMap));
        returnArray.put("permissionsText", JSON.toJSONString(returnTextMap));
        return returnArray;
    }

    @Override
    public Page<AdminRoleList> getAdminRolesByRoleId(long roleId, Integer page, Integer rowsPerPage) {
        List<AdminRoleList> adminRoleLists = new LinkedList<>();
        Page<AdminRole> adminRolePagination = adminRoleService.getAdminRolesByRoleId(
                roleId,
                page,
                rowsPerPage
        );
        for (AdminRole adminRole : adminRolePagination.getRecords()) {
            AdminRoleList adminRoleList = new AdminRoleList();
            adminRoleList.setAdminRole(adminRole);
            if (adminRole.getAdminId() > 0) {
                adminRoleList.setAdmin(adminService.getById(adminRole.getAdminId()));
            }
            adminRoleLists.add(adminRoleList);
        }
        Page<AdminRoleList> adminRoleListPage = new Page<>();
        adminRoleListPage.setRecords(adminRoleLists);
        adminRoleListPage.setTotal(adminRolePagination.getTotal());
        adminRoleListPage.setCurrent(Long.parseLong(String.valueOf(page)));
        adminRoleListPage.setSize(Long.parseLong(String.valueOf(rowsPerPage)));
        return adminRoleListPage;
    }
}
