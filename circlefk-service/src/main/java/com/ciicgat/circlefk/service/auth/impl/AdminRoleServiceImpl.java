package com.ciicgat.circlefk.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;
import com.ciicgat.circlefk.domain.dao.AdminRoleDao;
import com.ciicgat.circlefk.domain.entity.AdminRole;
import com.ciicgat.circlefk.protocol.request.adminrole.AdminRoleAddRequest;
import com.ciicgat.circlefk.service.auth.AdminRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoleServiceImpl.class);

    @Resource
    private AdminRoleDao adminRoleDao;

    @Override
    public Boolean grantRole(AdminRoleAddRequest adminRoleAddRequest) {
        if (this.getAdminRoleByAdminIdAndRoleId(adminRoleAddRequest.getAdminId(), adminRoleAddRequest.getRoleId()) != null) {
            throw new BusinessRuntimeException(-1, "已授权该角色");
        }
        AdminRole adminRole = new AdminRole();
        BeanUtils.copyProperties(adminRoleAddRequest, adminRole);
        adminRole.setTimeValidTo(adminRoleAddRequest.getTimeValidTo());
        if(adminRoleDao.insert(adminRole) != 1) {
            throw new BusinessRuntimeException(-1, "授权失败");
        }
        return true;
    }

    @Override
    public Boolean cancelGrantRole(Long adminId, Long roleId) {
        AdminRole adminRoleByAdminIdAndRoleId = this.getAdminRoleByAdminIdAndRoleId(adminId, roleId);
        if (adminRoleByAdminIdAndRoleId == null) {
            throw new BusinessRuntimeException(-1, "未授权该角色");
        }
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(adminId);
        adminRole.setRoleId(roleId);
        if(adminRoleDao.deleteById(adminRoleByAdminIdAndRoleId.getId()) != 1) {
            throw new BusinessRuntimeException(-1, "取消授权失败");
        }
        return true;
    }

    @Override
    public Page<AdminRole> getAdminRolesByRoleId(Long roleId, Integer page, Integer rowsPerPage) {
        QueryWrapper<AdminRole> adminRoleQueryWrapper = Wrappers.query();
        adminRoleQueryWrapper.lambda()
                .eq(AdminRole::getRoleId, roleId)
                .orderByDesc(AdminRole::getId);
        return adminRoleDao.selectPage(new Page<>(page, rowsPerPage), adminRoleQueryWrapper);
    }

    @Override
    public Boolean deleteById(Long id) {
        if (adminRoleDao.deleteById(id) != 1) {
            throw new BusinessRuntimeException(-1, "删除角色失败");
        }
        return true;
    }

    @Override
    public List<AdminRole> getAdminRolesByAdminId(Long adminId) {
        QueryWrapper<AdminRole> adminRoleQueryWrapper = Wrappers.query();
        adminRoleQueryWrapper.lambda()
                .eq(AdminRole::getAdminId, adminId);
        return adminRoleDao.selectList(adminRoleQueryWrapper);
    }

    @Override
    public AdminRole getAdminRoleById(Long id) {
        return adminRoleDao.selectById(id);
    }

    @Override
    public List<AdminRole> findAdminRoleByTimeValidToAndRoleType(String timeValidTo, Integer roleType) {
        QueryWrapper<AdminRole> adminRoleQueryWrapper = Wrappers.query();
        adminRoleQueryWrapper.lambda()
        .eq(AdminRole::getTimeValidTo, timeValidTo)
        .eq(AdminRole::getRoleType, roleType);
        return adminRoleDao.selectList(adminRoleQueryWrapper);
    }

    @Override
    public List<AdminRole> findAdminRoleByTimeValidTo(String timeValidTo) {
        QueryWrapper<AdminRole> adminRoleQueryWrapper = Wrappers.query();
        adminRoleQueryWrapper.lambda()
                .eq(AdminRole::getTimeValidTo, timeValidTo);
        return adminRoleDao.selectList(adminRoleQueryWrapper);
    }

    @Override
    public Boolean extendAdminRoleValidTime(Long id, String timeValidTo) {
        AdminRole adminRole = new AdminRole();
        adminRole.setId(id);
        adminRole.setTimeValidTo(timeValidTo);
        if (adminRoleDao.updateById(adminRole) != 1) {
            throw new BusinessRuntimeException(-1, "更新有效期时间失败");
        }
        return true;
    }

    @Override
    public AdminRole getAdminRoleByAdminIdAndRoleId(Long adminId, Long roleId) {
        QueryWrapper<AdminRole> adminRoleQueryWrapper = Wrappers.query();
        adminRoleQueryWrapper.lambda()
                .eq(AdminRole::getAdminId, adminId)
        .eq(AdminRole::getRoleId, roleId);
        return adminRoleDao.selectOne(adminRoleQueryWrapper);
    }

    @Override
    public Boolean changeRoleTypeAndTimeValidToById(Long id, Integer roleType) {
        AdminRole adminRole = new AdminRole();
        adminRole.setId(id);
        adminRole.setRoleType(roleType);
        if (adminRoleDao.updateById(adminRole) != 1) {
            throw new BusinessRuntimeException(-1, "更新角色类型失败");
        }
        return true;
    }

    @Override
    public Long staAdminRole(Long roleId) {
        QueryWrapper<AdminRole> adminRoleQueryWrapper = Wrappers.query();
        adminRoleQueryWrapper.lambda()
                .eq(AdminRole::getRoleId, roleId);
        return Long.valueOf(adminRoleDao.selectCount(adminRoleQueryWrapper));
    }

    @Override
    public List<AdminRole> getValidAdminRoles(Long adminId) {
        return adminRoleDao.getValidAdminRoles(adminId);
    }
}
