package com.ciicgat.circlefkbff.protocol.response.auth;


import com.ciicgat.circlefkbff.feign.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminRealm implements Serializable {
    private static final long serialVersionUID = 1L;
    private long adminId;
    private List<Role> roles;

    private Permission permissionEntity;

    private Permission permissionDisplay;

   }
