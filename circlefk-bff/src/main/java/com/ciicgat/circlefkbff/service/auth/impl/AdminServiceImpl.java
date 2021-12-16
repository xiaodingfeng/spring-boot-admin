package com.ciicgat.circlefkbff.service.auth.impl;

import com.ciicgat.circlefkbff.feign.api.FeignAdminService;
import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminUpdate;
import com.ciicgat.circlefkbff.service.auth.AdminRoleService;
import com.ciicgat.circlefkbff.service.auth.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Resource
    private FeignAdminService feignAdminService;

    @Resource
    private AdminRoleService adminRoleService;


    @Override
    public Long add(AdminAddRequest adminAddRequest) {
        return feignAdminService.add(adminAddRequest);
    }

    @Override
    public Admin getById(Long adminId) {
        return feignAdminService.getById(adminId);
    }

    @Override
    public Boolean updateAdmin(AdminUpdate adminUpdate) {
        return feignAdminService.updateAdmin(adminUpdate);
    }

    @Override
    public Boolean resetPassword(Long adminId, String password) {
        return feignAdminService.resetPassword(adminId, password);
    }

    @Override
    public Page<Admin> getAdminList(AdminListRequest adminListRequest) {
        return feignAdminService.getAdminList(adminListRequest);
    }

    @Override
    public Page<Admin> getAdminListByEnterpriseId(AdminListRequest adminListRequest) {
        return feignAdminService.getAdminListByEnterpriseId(adminListRequest);
    }

    @Override
    public Boolean checkAndResetPassword(Long adminId, String password, String newPassword) {
        return feignAdminService.checkAndResetPassword(adminId, password, newPassword);
    }

    @Override
    public List<Admin> getList(Admin admin) {
        return feignAdminService.getList(admin);
    }

    @Override
    public Boolean checkPassword(Long adminId, String password) {
        return feignAdminService.checkPassword(adminId, password);
    }
}
