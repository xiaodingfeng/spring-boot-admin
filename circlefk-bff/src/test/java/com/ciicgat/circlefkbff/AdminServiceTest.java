package com.ciicgat.circlefkbff;

import com.ciicgat.circlefkbff.feign.api.FeignAdminService;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminAddRequest;
import com.ciicgat.circlefkbff.protocol.request.admin.AdminListRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {

    @Resource
    FeignAdminService feignAdminService;

    @Test
    public void getList() {
        AdminListRequest adminListRequest = new AdminListRequest();
        adminListRequest.setEmail("11111");
        System.out.println(feignAdminService.getAdminList(adminListRequest));
    }

    @Test
    public void add() {
        AdminAddRequest adminAddRequest = new AdminAddRequest();
        adminAddRequest.setUsername("大白兔" + 1222);
        adminAddRequest.setPassword("123456");
        adminAddRequest.setMobile("123456133" + 22222);
        adminAddRequest.setName("小白" + 2222);
        adminAddRequest.setEnterpriseId(444L);
        System.out.println(feignAdminService.add(adminAddRequest));
    }


}
