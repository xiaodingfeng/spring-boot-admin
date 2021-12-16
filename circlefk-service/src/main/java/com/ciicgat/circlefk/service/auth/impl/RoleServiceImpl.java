package com.ciicgat.circlefk.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;
import com.ciicgat.circlefk.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefk.domain.dao.RoleDao;
import com.ciicgat.circlefk.domain.entity.AdminRole;
import com.ciicgat.circlefk.domain.entity.Role;
import com.ciicgat.circlefk.protocol.request.role.RoleAdd;
import com.ciicgat.circlefk.protocol.request.role.RoleEdit;
import com.ciicgat.circlefk.protocol.response.auth.AdminRealm;
import com.ciicgat.circlefk.protocol.response.auth.Permission;
import com.ciicgat.circlefk.service.auth.AdminRoleService;
import com.ciicgat.circlefk.service.auth.AdminService;
import com.ciicgat.circlefk.service.auth.RoleService;
import org.phprpc.util.AssocArray;
import org.phprpc.util.PHPSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Resource
    private RoleDao roleDao;

    @Resource
    private AdminRoleService adminRoleService;

    @Resource
    private AdminService adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Long addRoleInfo(RoleAdd roleAdd) {
        Role role = new Role();
        BeanUtils.copyProperties(roleAdd, role);
        int row = roleDao.insert(role);
        if (row != 1) {
            LOGGER.error("addRole failed (db error)");
            throw new BusinessRuntimeException(-1, "角色新增失败");
        }
        return role.getId();
    }

    public void phpArrayToPermission(Permission rolePermissionDisplay, AssocArray objectTxt) {
        AssocArray menuTxt = (AssocArray) objectTxt.get("menu");
        if (menuTxt == null) {
            LOGGER.warn("menu property missing in {}", objectTxt.toHashMap());
        }
        ArrayList<byte[]> menusPhpTxt = (menuTxt == null) ?  new ArrayList<>() : menuTxt.toArrayList();
        for (byte[] bytes : menusPhpTxt) {
            rolePermissionDisplay.getMenus().add(new String(bytes));
        }
        AssocArray rightsPhpTxt = (AssocArray) objectTxt.get("rights");
        if (rightsPhpTxt == null) {
            LOGGER.warn("rights property missing in {}", objectTxt.toHashMap());
        }
        LinkedHashMap<String, AssocArray> linkedHashMapTxt = (rightsPhpTxt == null) ? new LinkedHashMap<>() : rightsPhpTxt.toLinkedHashMap();
        for (Map.Entry<String, AssocArray> entry : linkedHashMapTxt.entrySet()) {
            String key = entry.getKey();

            Set<String> stringSet = rolePermissionDisplay.getRights().computeIfAbsent(key, k -> new HashSet<String>());
            ArrayList<byte[]> arrayList = entry.getValue().toArrayList();
            for (byte[] bytes : arrayList) {
                stringSet.add(new String(bytes));
            }
        }
    }
    private void phpUnserialize(Role role) {
        PHPSerializer p = new PHPSerializer();
        try {
            // 对permissions反序列化
            Permission rolePermissionEntity = new Permission();
            role.setPermissionEntity(rolePermissionEntity);
            AssocArray object = (AssocArray) p.unserialize(role.getPermissions().getBytes());
            this.phpArrayToPermission(rolePermissionEntity, object);

            // 对permissionsText反序列化
            Permission rolePermissionDisplay = new Permission();//权限实体
            role.setPermissionDisplay(rolePermissionDisplay);
            AssocArray objectTxt = (AssocArray) p.unserialize(role.getPermissionsText().getBytes());
            this.phpArrayToPermission(rolePermissionDisplay, objectTxt);
        }  catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public Integer updateRoleInfo(RoleEdit roleEdit) {
        Role role = new Role();
        BeanUtils.copyProperties(roleEdit, role);
        int row = roleDao.updateById(role);
        if (row != 1) {
            LOGGER.error("editRole failed (db error)");
            throw new BusinessRuntimeException(-1, "角色修改失败");
        }
        return row;
    }

    @Override
    public Boolean deleteRoleById(Long id) {
        int row = roleDao.deleteById(id);
        if (row != 1) {
            LOGGER.error("deleteRole failed (db error)");
            throw new BusinessRuntimeException(-1, "角色删除失败");
        }
        return true;
    }

    @Override
    public List<Role> getRoleListByEnterpriseId(Long enterpriseId) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.lambda().eq(Role::getEnterpriseId, enterpriseId)
                .orderByDesc(Role::getId);
        List<Role> roleData = roleDao.selectList(queryWrapper);
        for (Role role : roleData) {
            // 先统计当前已授权人数
            role.setAuthQty(adminRoleService.staAdminRole(role.getId()));
            role.setPermissionEntity(JSON.parse(role.getPermissions(),Permission.class));
            role.setPermissionDisplay(JSON.parse(role.getPermissionsText(),Permission.class));
        }
        return roleData;
    }

    @Override
    public Role get(Long id) {
        Role role = roleDao.selectById(id);
        role.setPermissionEntity(JSON.parse(role.getPermissions(),Permission.class));
        role.setPermissionDisplay(JSON.parse(role.getPermissionsText(),Permission.class));
        return role;
    }

    @Override
    public List<Role> getByIds(List<Long> ids) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.lambda()
                .in(Role::getId, ids);
        List<Role> roles = roleDao.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        for (Role role : roles) {
            role.setPermissionEntity(JSON.parse(role.getPermissions(),Permission.class));
            role.setPermissionDisplay(JSON.parse(role.getPermissionsText(),Permission.class));
        }
        return roles;
    }

    @Override
    public AdminRealm getAdminRealm(Long adminId) {
        String s = stringRedisTemplate.opsForValue().get("app_getAdminRealm_en_" + adminId);

        if (StringUtils.hasText(s)) {
            return JSON.parse(s, AdminRealm.class);
        }

        // 这里现在需要获取有效期内的角色
        List<AdminRole> adminRoleList = adminRoleService.getValidAdminRoles(adminId);
        if (null == adminRoleList) {
            return null;
        }

        List<Long> roleIds = new ArrayList<>();
        for (AdminRole adminRole : adminRoleList) {
            roleIds.add(adminRole.getRoleId());
        }

        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }

        List<Role> roleList = this.getByIds(roleIds);

        if (CollectionUtils.isEmpty(roleList)) {
            return null;
        }

        AdminRealm result = new AdminRealm();
        result.setAdminId(adminId);

        for (Role role : roleList) {
            try {
                role.setPermissionEntity(JSON.parse(role.getPermissions(),Permission.class));
                role.setPermissionDisplay(JSON.parse(role.getPermissionsText(),Permission.class));
            } catch (Exception e) {
                LOGGER.error(",adminId[" + adminId + "]", e);
            }
        }
        Permission permissionEntity = new Permission();    //权限定义
        Permission permissionDisplay = new Permission();//权限实体

        result.setPermissionEntity(permissionEntity);
        result.setPermissionDisplay(permissionDisplay);

        for (Role role : roleList) {
            permissionEntity.getMenus().addAll(role.getPermissionEntity().getMenus());
            concatPermission(permissionEntity, role, 1);
            permissionDisplay.getMenus().addAll(role.getPermissionDisplay().getMenus());
            concatPermission(permissionDisplay, role, 2);
        }
        result.setRoles(roleList);
        stringRedisTemplate.opsForValue().set("app_getAdminRealm_en_" + adminId, JSON.toJSONString(result), 20, TimeUnit.MINUTES);
        return result;
    }

    private void concatPermission(Permission permission, Role role, int type) {
        Map<String, Set<String>> roleRights;
        if (type == 1) {
            roleRights = role.getPermissionEntity().getRights();
        } else {
            roleRights = role.getPermissionDisplay().getRights();
        }

        Map<String, Set<String>> resultMap = permission.getRights();
        Iterator<Map.Entry<String, Set<String>>> iterator = roleRights.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Set<String>> next = iterator.next();
            String nextKey = next.getKey();
            boolean containsKey = resultMap.containsKey(nextKey);
            Set<String> newStrings = roleRights.get(nextKey);
            if (containsKey) {
                Set<String> strings = resultMap.get(nextKey);
                strings.addAll(newStrings);
                resultMap.put(nextKey, strings);
            } else {
                resultMap.put(nextKey, newStrings);
            }
        }
    }
}
