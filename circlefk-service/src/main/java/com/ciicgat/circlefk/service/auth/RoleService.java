package com.ciicgat.circlefk.service.auth;

import com.ciicgat.circlefk.domain.entity.Role;
import com.ciicgat.circlefk.protocol.request.role.RoleAdd;
import com.ciicgat.circlefk.protocol.request.role.RoleEdit;
import com.ciicgat.circlefk.protocol.response.auth.AdminRealm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleService {
    Long addRoleInfo(RoleAdd roleAdd);
    Integer updateRoleInfo(RoleEdit roleEdit);
    Boolean deleteRoleById(Long id);
    List<Role> getRoleListByEnterpriseId(Long enterpriseId);
    Role get(Long id);
    List<Role> getByIds(List<Long> ids);
    AdminRealm getAdminRealm(Long adminId);
}
