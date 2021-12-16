package com.ciicgat.circlefk.auth;

import com.ciicgat.circlefk.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefk.protocol.request.adminrole.AdminRoleAddRequest;
import com.ciicgat.circlefk.service.auth.AdminRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminRoleTest {

    @Resource
    private AdminRoleService adminRoleService;

    @Test
    public void getList() {
    }

    @Test
    public void add() {
        AdminRoleAddRequest adminRoleAddRequest = new AdminRoleAddRequest();
        adminRoleAddRequest.setRoleId(1L);
        adminRoleAddRequest.setAdminId(2L);
        adminRoleAddRequest.setRoleType(1);
        adminRoleAddRequest.setTimeValidTo("2021-12-03");
        adminRoleService.grantRole(adminRoleAddRequest);
    }

    @Test
    public void findAdminRoleByTimeValidTo() {
        System.out.println(JSON.toJSONString(adminRoleService.findAdminRoleByTimeValidTo("2021-12-02")));
    }
}
