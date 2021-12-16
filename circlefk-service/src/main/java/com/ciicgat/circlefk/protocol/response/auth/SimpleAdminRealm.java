package com.ciicgat.circlefk.protocol.response.auth;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SimpleAdminRealm {

    /**
     * admin对应的权限列表
     */
    private Map<String, List<String>> rightList;

    /**
     * admin对应的菜单列表
     */
    private List<String> menuList;

}
