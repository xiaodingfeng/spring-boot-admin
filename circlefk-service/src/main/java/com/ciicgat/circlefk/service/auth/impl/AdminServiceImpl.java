package com.ciicgat.circlefk.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;
import com.ciicgat.circlefk.common.sdk.lang.exception.Errors;
import com.ciicgat.circlefk.domain.dao.AdminDao;
import com.ciicgat.circlefk.domain.entity.Admin;
import com.ciicgat.circlefk.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminUpdate;
import com.ciicgat.circlefk.protocol.request.adminrole.AdminRoleAddRequest;
import com.ciicgat.circlefk.service.auth.AdminRoleService;
import com.ciicgat.circlefk.service.auth.AdminService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Resource
    private AdminDao adminDao;

    @Resource
    private AdminRoleService adminRoleService;

    @Transactional
    @Override
    public Long add(AdminAddRequest adminAddRequest) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminAddRequest, admin);

        if (!StringUtils.hasText(admin.getName()) || !StringUtils.hasText(admin.getPassword())
                || !StringUtils.hasText(admin.getUsername()) || admin.getEnterpriseId() == null) {
            LOGGER.warn("createPartner failed, PARAMETER_ERROR:{}", admin);
            throw new BusinessRuntimeException(Errors.PARAMETER_ERROR);
        }

        admin.setMobile(!StringUtils.hasText(admin.getMobile()) ? null : admin.getMobile());
        admin.setEmail(!StringUtils.hasText(admin.getEmail()) ? null : admin.getEmail());
        checkUsernameAvailablity(admin.getUsername());
        checkMobileAvailablity(admin.getMobile());
        String password = admin.getPassword();
        admin.setGender(admin.getGender() == null ? 1 : admin.getGender());
        // 添加帐号
        int row = adminDao.insert(admin); // 返回影响行数,并且成功的话,会带上adminId
        if (row != 1) {
            LOGGER.error("addAdmin failed (db error)");
            throw new BusinessRuntimeException(Errors.USER_ADD_FAIL);
        }
        String encryptedPassword = DigestUtils.sha1Hex(DigestUtils.sha1Hex(admin.getId() + password));

        Admin admin1 = new Admin();
        admin1.setId(admin.getId());
        admin1.setPassword(encryptedPassword);
        int row1 = adminDao.updateById(admin1);
        if (row1 != 1) {
            LOGGER.error("setPassword failed (db error)");
            throw new BusinessRuntimeException(Errors.USER_ADD_FAIL);
        }

        AdminRoleAddRequest adminRoleAddRequest = new AdminRoleAddRequest();
        adminRoleAddRequest.setAdminId(admin.getId());
        adminRoleAddRequest.setRoleId(adminAddRequest.getRoleId());
        adminRoleAddRequest.setRoleType(1);
        adminRoleAddRequest.setTimeValidTo("2099-01-01");
        if (!adminRoleService.grantRole(adminRoleAddRequest)) {
            throw new BusinessRuntimeException(Errors.USER_ADD_FAIL);
        }

        return admin.getId();
    }

    @Override
    public Boolean resetPassword(Long adminId, String password) {
        Admin admin = this.getById(adminId);
        if (admin == null) {
            // 如果不存在该管理员
            LOGGER.error("resetPassword failed adminId:{} not exist", adminId);
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }
        String newPassword = DigestUtils.sha1Hex(DigestUtils.sha1Hex(adminId + password));

        admin = new Admin();
        admin.setId(adminId);
        admin.setPassword(newPassword);
        int row = adminDao.updateById(admin);
        if (row != 1) {
            LOGGER.error("resetPassword failed (db error)");
            throw new BusinessRuntimeException(Errors.PASSWORD_RESET_FAIL);
        }
        return true;
    }

    @Override
    public Admin getById(Long adminId) {
        return adminDao.selectById(adminId);
    }

    public Boolean updateAdmin(AdminUpdate adminUpdate) {
        // 先通过Id获取到修改之前的账户状态
        // 然后判断 -> 如果手机号做了修改，则需要验证手机号是否唯一
        Admin adminData = this.getById(adminUpdate.getAdminId());
        if (adminData == null) {
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }
        if (adminUpdate.getMobile() != null && !adminUpdate.getMobile().equals(adminData.getMobile())) {
            checkMobileAvailablity(adminUpdate.getMobile());
        }

        Admin admin = new Admin();
        admin.setId(adminUpdate.getAdminId());
        admin.setName(adminUpdate.getName());
        admin.setStatus(adminUpdate.getStatus());
        if (StringUtils.hasText(adminUpdate.getMobile())) {
            admin.setMobile(adminUpdate.getMobile());
        }
        if (StringUtils.hasText(adminUpdate.getEmail())) {
            admin.setEmail(adminUpdate.getEmail());
        }
        return adminDao.updateById(admin) == 1;
    }

    public Page<Admin> getAdminList(AdminListRequest adminListRequest) {
        QueryWrapper<Admin> userQueryWrapper = Wrappers.query();
        userQueryWrapper.lambda()
                .eq(StringUtils.hasText(adminListRequest.getMobile()),Admin::getMobile, adminListRequest.getMobile())
        .eq(StringUtils.hasText(adminListRequest.getEmail()),Admin::getEmail, adminListRequest.getEmail())
                .eq(adminListRequest.getEnterpriseId() != null, Admin::getEnterpriseId, adminListRequest.getEnterpriseId())
                .eq(adminListRequest.getStatus() != null, Admin::getStatus, adminListRequest.getStatus())
                .eq(StringUtils.hasText(adminListRequest.getName()), Admin::getName, adminListRequest.getName())
                .like(StringUtils.hasText(adminListRequest.getNameSearch()), Admin::getName, adminListRequest.getNameSearch())
                .eq(StringUtils.hasText(adminListRequest.getUsername()),Admin::getUsername, adminListRequest.getUsername())
                .like(StringUtils.hasText(adminListRequest.getUsernameSearch()), Admin::getUsername, adminListRequest.getUsernameSearch())
                .ge(StringUtils.hasText(adminListRequest.getTimeModifiedStart()), Admin::getTimeModified, adminListRequest.getTimeModifiedStart())
                .le(StringUtils.hasText(adminListRequest.getTimeModifiedEnd()), Admin::getTimeModified, adminListRequest.getTimeModifiedEnd())
        .notIn(!CollectionUtils.isEmpty(adminListRequest.getIdNotInList()), Admin::getId, adminListRequest.getIdNotInList())
        .orderByDesc(Admin::getId);
        return adminDao.selectPage(new Page<>(adminListRequest.getPage(), adminListRequest.getRowsPerPage()), userQueryWrapper);
    }

    @Override
    public Page<Admin> getAdminListByEnterpriseId(AdminListRequest adminListRequest) {
        if (adminListRequest.getEnterpriseId() == null) {
            throw new BusinessRuntimeException(-1, "企业ID不为空！");
        }
        return this.getAdminList(adminListRequest);
    }

    private void checkUsernameAvailablity(String username) {
        if (!StringUtils.hasText(username)) {
            throw new BusinessRuntimeException(Errors.ADD_USER_USERNAME_IS_EMPTY);
        }
        QueryWrapper<Admin> userQueryWrapper = Wrappers.query();
        userQueryWrapper.eq("username", username);
        Integer count = adminDao.selectCount(userQueryWrapper);
        if (count != 0) {
            LOGGER.warn("checkUsername failed, username:{} exists", username);
            throw new BusinessRuntimeException(Errors.USERNAME_EXIST);
        }
    }

    private void checkMobileAvailablity(String mobile) {
        if (!StringUtils.hasText(mobile)) {
            return;
        }
        QueryWrapper<Admin> userQueryWrapper = Wrappers.query();
        userQueryWrapper.eq("mobile", mobile);
        Integer count = adminDao.selectCount(userQueryWrapper);
        if (count != 0) {
            throw new BusinessRuntimeException(Errors.MOBILE_EXIST);
        }
    }

    @Override
    public Boolean checkPassword(long adminId, String password) {
        Admin admin = this.getById(adminId);
        if (admin == null) {
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }
        String encryptedPassword = DigestUtils.sha1Hex(DigestUtils.sha1Hex(admin.getId() + password));
        return encryptedPassword.equals(admin.getPassword());
    }

    @Override
    public List<Admin> getList(Admin admin) {
        if (!StringUtils.hasText(admin.getUsername()) && !StringUtils.hasText(admin.getMobile())) {
            throw new BusinessRuntimeException(Errors.PARAMETER_ERROR);
        }
        QueryWrapper<Admin> userQueryWrapper = Wrappers.query();
        userQueryWrapper.lambda()
                .eq(StringUtils.hasText(admin.getUsername()),Admin::getUsername, admin.getUsername())
                .eq(StringUtils.hasText(admin.getMobile()),Admin::getMobile, admin.getMobile());
       return adminDao.selectList(userQueryWrapper);
    }
}
