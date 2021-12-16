package com.ciicgat.circlefkbff.web.annotation;


import com.ciicgat.circlefkbff.common.sdk.lang.exception.BusinessRuntimeException;
import com.ciicgat.circlefkbff.common.sdk.lang.exception.Errors;
import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefkbff.common.sdk.lang.utils.JWTUtil;
import com.ciicgat.circlefkbff.feign.api.FeignAdminRealmService;
import com.ciicgat.circlefkbff.feign.api.FeignVerCodeService;
import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.protocol.response.auth.AdminRealm;
import com.ciicgat.circlefkbff.service.auth.AdminService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO  当前验证权限的类需要优化

@Aspect
@Component
public class AuthInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

    @Resource
    private AdminService adminService;

    @Resource
    private FeignAdminRealmService feignAdminRealmService;

    @Resource
    private FeignVerCodeService feignVerCodeService;

    @Resource
    private JWTUtil jwtUtil;

    @Pointcut("@annotation(com.ciicgat.circlefkbff.web.annotation.Auth))")
    public void pointCut() {
        // 切点
    }

    @Before("pointCut() && @annotation(auth)")
    public void authExecute(Auth auth) {
        if (auth.value().equals("")) {
            // 未设置权限，则直接通过
            return;
        }

        LOGGER.info("验证的权限：" + auth.value());

        // 获取当前用户
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();


        String token = request.getHeader("Authorization");

        if (!StringUtils.hasText(token)) {
            throw new BusinessRuntimeException(Errors.NO_PRIVILEGE_ERROR);
        }

        if (!feignVerCodeService.validateTokenRedis(token.replace(jwtUtil.getTokenPrefix(), ""))) {
            throw new BusinessRuntimeException(-1100110011, "token验证失败或已失效");
        }

        Admin tokenAdmin = JSON.parse(jwtUtil.validateToken(token), Admin.class);


        Admin admin = adminService.getById(tokenAdmin.getId());
        if (admin == null) {
            // 未获取到登录用户
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }

        LOGGER.info("管理员信息：adminId:" + admin.getId() + ",username:" + admin.getUsername() + ",name:" + admin.getName());

        // 获取管理员权限
        List<String> userRights = getUserRights(admin.getId());

        LOGGER.info("所有权限：");
        LOGGER.info(JSON.toJSONString(userRights));

        // 验证权限
        if(userRights.contains(auth.value())){
            LOGGER.info("权限验证成功");
            return;
        }
        throw new BusinessRuntimeException(Errors.NO_PRIVILEGE_ERROR);
    }

    private List<String> getUserRights(Long adminId){
        AdminRealm adminRealm =  feignAdminRealmService.getAdminRealm(adminId);
        if (adminRealm == null) {
            throw new BusinessRuntimeException(Errors.NO_PRIVILEGE_ERROR);
        }
        List<String> rightList = new ArrayList<>();

        adminRealm.getRoles().forEach(x -> x.getPermissionEntity().getRights().forEach((key, value) -> rightList.addAll(value)));
        return rightList.stream().distinct().collect(Collectors.toList());
    }


}
