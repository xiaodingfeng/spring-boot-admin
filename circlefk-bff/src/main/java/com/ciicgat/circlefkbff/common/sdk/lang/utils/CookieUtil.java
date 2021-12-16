package com.ciicgat.circlefkbff.common.sdk.lang.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);


    public static void addCookie(HttpServletResponse servletResponse, String domain, String cookieName, String cookieValue) {
        add(servletResponse, domain, cookieName, cookieValue, 360000);
    }

    private static void add(HttpServletResponse servletResponse, String domain, String cookieName, String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        // cookie.setVersion(1); //兼容chrome
        // cookie.setMaxAge(-1); //退出删除
        cookie.setMaxAge(maxAge);
        servletResponse.addCookie(cookie);
    }

    public static String getCookieVal(HttpServletRequest servletRequest, String cookieName) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookie_Name = cookie.getName();
                LOGGER.info("cookie获取:" + servletRequest.getRequestURI() + ",cookie:" + cookie_Name);
                if (cookie_Name.equals(cookieName)) {
                    String val = cookie.getValue();
                    if (val != null) {
                        return val;
                    }
                }
            }
        }
        return "";
    }

    public static boolean exitsCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String cookieName, String newValue) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName) && cookie.getValue() != null) {
                    cookie.setValue(newValue);
                    cookie.setMaxAge(360000);
                    cookie.setVersion(1);
                    servletResponse.addCookie(cookie);
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String cookieName) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName) && cookie.getValue() != null) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    servletResponse.addCookie(cookie);
                    break;
                }
            }
        }
    }
}
