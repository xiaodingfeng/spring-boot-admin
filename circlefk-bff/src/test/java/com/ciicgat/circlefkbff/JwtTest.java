package com.ciicgat.circlefkbff;

import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefkbff.common.sdk.lang.utils.JWTUtil;
import com.ciicgat.circlefkbff.feign.entity.Admin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {

    @Resource
    JWTUtil jwtUtil;
    @Test
    public void jwtAdd() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUsername("肖定峰");
        System.out.println(JSON.toJSONString(admin));
        String hhh_gggh = jwtUtil.createToken(JSON.toJSONString(admin));
        System.out.println(hhh_gggh);

//        System.out.println(jwtUtil.validateToken("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxX3VzZXJuYW1lIiwiZXhwIjoxNjM4NzY3ODEyfQ.HOOPQpWSP_C_siOWGxZ1aovKb2yM2iRvLf2bOug-Ur3yqDhK64GMZaa1aHzsPYfM7QutJfLm2Lk-XX1n5kS7LA"));
    }

    //创建日志对象
    Logger logger = LogManager.getLogger(this.getClass());
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JwtTest.class);
    @Test
    public void test2() {
        Admin admin = null;

        Exception exception = new Exception();

//        LOGGER.info(JSON.toJSONString(admin));
        LOGGER.error("我是log4j2的错误信息");
    }
}
