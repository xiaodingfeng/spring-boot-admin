package com.ciicgat.circlefk.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ciicgat.circlefk.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefk.domain.entity.Admin;
import com.ciicgat.circlefk.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefk.protocol.request.admin.AdminListRequest;
import com.ciicgat.circlefk.service.auth.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminTest {
    @Resource
    private AdminService adminService;

    @Test
    public void getList() {
        AdminListRequest adminAddRequest1 = new AdminListRequest();
        adminAddRequest1.setPage(1);
        adminAddRequest1.setRowsPerPage(50);
        Page<Admin> adminList = adminService.getAdminList(adminAddRequest1);
        System.out.println(JSON.toJSONString(adminList));

    }

    @Test
    public void add() {
        for (int i = 0; i < 10; i++) {
            AdminAddRequest adminAddRequest = new AdminAddRequest();
            adminAddRequest.setUsername("大白兔" + i);
            adminAddRequest.setPassword("123456");
            adminAddRequest.setMobile("123456133" + i);
            adminAddRequest.setName("小白" + i);
//            adminAddRequest.setEnterpriseId(1L);
            adminService.add(adminAddRequest);
        }

    }
}
