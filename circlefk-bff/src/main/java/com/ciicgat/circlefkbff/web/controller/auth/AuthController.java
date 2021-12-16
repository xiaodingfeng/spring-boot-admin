package com.ciicgat.circlefkbff.web.controller.auth;

import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.common.sdk.lang.convert.ErrorCode;
import com.ciicgat.circlefkbff.common.sdk.lang.exception.BusinessRuntimeException;
import com.ciicgat.circlefkbff.common.sdk.lang.exception.Errors;
import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefkbff.common.sdk.lang.utils.JWTUtil;
import com.ciicgat.circlefkbff.feign.api.FeignAdminRealmService;
import com.ciicgat.circlefkbff.feign.api.FeignVerCodeService;
import com.ciicgat.circlefkbff.feign.entity.Admin;
import com.ciicgat.circlefkbff.protocol.request.LoginRequest;
import com.ciicgat.circlefkbff.protocol.response.auth.AdminRealm;
import com.ciicgat.circlefkbff.protocol.response.auth.LoginBackResponse;
import com.ciicgat.circlefkbff.service.auth.AdminService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AdminService adminService;

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private FeignVerCodeService feignVerCodeService;

    @Resource
    private FeignAdminRealmService feignAdminRealmService;

    @PostMapping(value = "getcaptcha")
    public ApiResponse<String> getCaptcha(HttpServletResponse response, HttpServletRequest request) {

        HttpSession session = request.getSession();

        if(session == null) {
            throw new BusinessRuntimeException(ErrorCode.REQUEST_BLOCK);
        }
        return ApiResponse.success(feignVerCodeService.createCaptcha(session.getId(), 6));
    }

    /**
     * 登录校验
     * @param request
     * @param loginRequest
     * @return
     */
    @PostMapping(value = "login")
    public ApiResponse<LoginBackResponse> login(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

//        HttpSession session = request.getSession();
//
//        if(session == null) {
//            throw new BusinessRuntimeException(ErrorCode.REQUEST_BLOCK);
//        }
//        String verCode = feignVerCodeService.getVerCode(Long.valueOf(session.getId()));
//
//        if (!Objects.equals(verCode, loginRequest.getVerifyCode())) {
//            throw new BusinessRuntimeException(Errors.VERIFY_CODE_ERROR);
//        }




        Admin admin = new Admin();
        admin.setUsername(loginRequest.getUsername());

        List<Admin> list = adminService.getList(admin);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }
        if (list.size() > 1) {
            throw new BusinessRuntimeException(-1, "用户名重复");
        }

        Boolean aBoolean = adminService.checkPassword(list.get(0).getId(), loginRequest.getPassword());

        if (!aBoolean) {
            throw new BusinessRuntimeException(Errors.USER_PASSWORD_ERROR);
        }
        if (list.get(0).getStatus() == 2) {
            throw new BusinessRuntimeException(-1, "用户已被禁用，请联系管理员");
        }
        admin.setId(list.get(0).getId());
        String token = jwtUtil.createToken(JSON.toJSONString(admin));
//        Cookie cookie = new Cookie("HHHHHH", token);
//        cookie.setPath("/");
//        cookie.setDomain("localhost");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//
//        cookie.setMaxAge(1);
//        response.addCookie(cookie);


//        String cookieVal = CookieUtil.getCookieVal(request, "token");
//        System.out.println(cookieVal);

//        CookieUtil.addCookie(response,"localhost","");


        feignVerCodeService.addToken(token,jwtUtil.getExpireTime() / 1000 - 1);
        LoginBackResponse loginBackResponse = new LoginBackResponse();
        loginBackResponse.setState(request.getRequestURI());
        loginBackResponse.setAccess_token(token);
        loginBackResponse.setExpires_in(jwtUtil.getExpireTime() / 1000);
        loginBackResponse.setToken_type(jwtUtil.getTokenPrefix().replaceAll(" ", ""));
        return ApiResponse.success(loginBackResponse);
    }

    @PostMapping(value = "getAdminInfo")
    public ApiResponse<Admin> getAdminInfo(@RequestHeader("Authorization") String token) {
        if (!StringUtils.hasText(token)) {
            throw new BusinessRuntimeException(Errors.NO_PRIVILEGE_ERROR);
        }

        Admin tokenAdmin = JSON.parse(jwtUtil.validateToken(token), Admin.class);
        Admin admin = adminService.getById(tokenAdmin.getId());
        if (admin == null) {
            // 未获取到登录用户
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }
        return ApiResponse.success(admin);
    }

    @PostMapping(value = "getAdminRealmInfo")
    public ApiResponse<AdminRealm> getAdminRealmInfo(@RequestHeader("Authorization") String token) {
        if (!StringUtils.hasText(token)) {
            throw new BusinessRuntimeException(Errors.NO_PRIVILEGE_ERROR);
        }

        Admin tokenAdmin = JSON.parse(jwtUtil.validateToken(token), Admin.class);
        Admin admin = adminService.getById(tokenAdmin.getId());
        if (admin == null) {
            // 未获取到登录用户
            throw new BusinessRuntimeException(Errors.ADMIN_NOT_EXIST);
        }

        return ApiResponse.success(feignAdminRealmService.getAdminRealm(admin.getId()));
    }
}
