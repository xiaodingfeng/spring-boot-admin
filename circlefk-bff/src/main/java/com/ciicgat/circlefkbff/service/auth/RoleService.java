package com.ciicgat.circlefkbff.service.auth;

import com.ciicgat.circlefkbff.feign.entity.Role;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.role.RoleAddRequest;
import com.ciicgat.circlefkbff.protocol.request.role.RoleEditRequest;
import com.ciicgat.circlefkbff.protocol.response.auth.AdminRoleList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleService {
    Long addRoleInfo(RoleAddRequest request);
    Integer updateRoleInfo(RoleEditRequest roleEditRequest);
    Boolean deleteRoleById(Long id);
    List<Role> getRoleListByEnterpriseId(Long enterpriseId);
    Role get(Long id);
    Page<AdminRoleList> getAdminRolesByRoleId(long roleId, Integer page, Integer rowsPerPage);
}
