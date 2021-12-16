package com.ciicgat.circlefkbff.protocol.response.auth;


import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.feign.entity.AdminRole;

public class AdminRoleList {

    private Admin admin;

    private AdminRole adminRole;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public AdminRole getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(AdminRole adminRole) {
        this.adminRole = adminRole;
    }
}
