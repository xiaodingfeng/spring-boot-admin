package com.ciicgat.circlefk.service.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ciicgat.circlefk.domain.entity.Admin;
import com.ciicgat.circlefk.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminUpdate;
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
    Boolean checkPassword(long adminId, String password);
    List<Admin> getList(Admin admin);
}
