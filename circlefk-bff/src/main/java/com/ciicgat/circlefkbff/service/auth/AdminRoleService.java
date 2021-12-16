package com.ciicgat.circlefkbff.service.auth;

import com.ciicgat.circlefkbff.feign.entity.AdminRole;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.adminrole.AdminRoleAddRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminRoleService {

    Boolean grantRole(AdminRoleAddRequest adminRoleAddRequest);

    Boolean cancelGrantRole(Long adminId, Long roleId);

     Page<AdminRole> getAdminRolesByRoleId(
            Long roleId,
            Integer page,
            Integer rowsPerPage);

    Boolean deleteById(Long id);

    List<AdminRole> getAdminRolesByAdminId(Long adminId);

    AdminRole getAdminRoleById(Long id);

    List<AdminRole> findAdminRoleByTimeValidToAndRoleType(String timeValidTo, Integer roleType);
    List<AdminRole> findAdminRoleByTimeValidTo(String timeValidTo);

    Boolean extendAdminRoleValidTime(Long id, String timeValidTo);

    AdminRole getAdminRoleByAdminIdAndRoleId(Long adminId, Long roleId);

    Boolean changeRoleTypeAndTimeValidToById(Long id, Integer roleType);
}
