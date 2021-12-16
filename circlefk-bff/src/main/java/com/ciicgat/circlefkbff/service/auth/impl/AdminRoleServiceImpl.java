package com.ciicgat.circlefkbff.service.auth.impl;

import com.ciicgat.circlefkbff.feign.api.FeignAdminRoleService;
import com.ciicgat.circlefkbff.feign.entity.AdminRole;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.adminrole.AdminRoleAddRequest;
import com.ciicgat.circlefkbff.service.auth.AdminRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoleServiceImpl.class);

    @Resource
    private FeignAdminRoleService feignAdminRoleService;

    @Override
    public Boolean grantRole(AdminRoleAddRequest adminRoleAddRequest) {
        adminRoleAddRequest.setRoleType(1);
        adminRoleAddRequest.setTimeValidTo("2099-01-01");
        return feignAdminRoleService.grantRole(adminRoleAddRequest);
    }

    @Override
    public Boolean cancelGrantRole(Long adminId, Long roleId) {
        return feignAdminRoleService.cancelGrantRole(adminId, roleId);
    }

    @Override
    public Page<AdminRole> getAdminRolesByRoleId(Long roleId, Integer page, Integer rowsPerPage) {
        return feignAdminRoleService.getAdminRolesByRoleId(roleId, page, rowsPerPage);
    }

    @Override
    public Boolean deleteById(Long id) {
        return feignAdminRoleService.deleteById(id);
    }

    @Override
    public List<AdminRole> getAdminRolesByAdminId(Long adminId) {
        return feignAdminRoleService.getAdminRolesByAdminId(adminId);
    }

    @Override
    public AdminRole getAdminRoleById(Long id) {
        return feignAdminRoleService.getAdminRoleById(id);
    }

    @Override
    public List<AdminRole> findAdminRoleByTimeValidToAndRoleType(String timeValidTo, Integer roleType) {
        return feignAdminRoleService.findAdminRoleByTimeValidToAndRoleType(timeValidTo, roleType);
    }

    @Override
    public List<AdminRole> findAdminRoleByTimeValidTo(String timeValidTo) {
        return feignAdminRoleService.findAdminRoleByTimeValidTo(timeValidTo);
    }

    @Override
    public Boolean extendAdminRoleValidTime(Long id, String timeValidTo) {
        return feignAdminRoleService.extendAdminRoleValidTime(id, timeValidTo);
    }

    @Override
    public AdminRole getAdminRoleByAdminIdAndRoleId(Long adminId, Long roleId) {
        return feignAdminRoleService.getAdminRoleByAdminIdAndRoleId(adminId, roleId);
    }

    @Override
    public Boolean changeRoleTypeAndTimeValidToById(Long id, Integer roleType) {
        return feignAdminRoleService.changeRoleTypeAndTimeValidToById(id, roleType);
    }
}
