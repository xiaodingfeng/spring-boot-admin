package com.ciicgat.circlefk.auth;

import com.ciicgat.circlefk.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefk.protocol.request.role.RoleAdd;
import com.ciicgat.circlefk.service.auth.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTest {
    @Resource
    private RoleService roleService;

    @Test
    public void add() {
        RoleAdd roleAdd = new RoleAdd();
        roleAdd.setName("小白兔权限");
        roleAdd.setDescription("描述嗷嗷嗷嗷");
        roleAdd.setPermissions("1111");
        roleAdd.setPermissionsText("addadad");
        roleService.addRoleInfo(roleAdd);
    }

    @Test
    public void getRoleList() {
        System.out.println(JSON.toJSONString(roleService.getRoleListByEnterpriseId(1L)));
    }
}
