package com.ciicgat.circlefkbff.service.auth;

import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.protocol.Page;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminUpdate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminService {
    Long add(AdminAddRequest adminAddRequest);
    Admin getById(Long adminId);
    Boolean updateAdmin(AdminUpdate adminUpdate);
    Boolean resetPassword(Long adminId, String password);
    Page<Admin> getAdminList(AdminListRequest adminListRequest);
    Page<Admin> getAdminListByEnterpriseId(AdminListRequest adminListRequest);
    Boolean checkAndResetPassword(Long adminId, String password, String newPassword);
    List<Admin> getList(Admin admin);
    Boolean checkPassword(Long adminId, String password);
}
