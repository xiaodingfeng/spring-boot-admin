package com.ciicgat.circlefkbff.feign.entity;

import com.ciicgat.circlefkbff.protocol.response.auth.Permission;
import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long enterpriseId;
    private String name;
    private String description;
    private String permissions;
    private String permissionsText;
    private String timeCreated;
    private String timeModified;

    private Permission permissionEntity;
    private Permission permissionDisplay;
    private Long authQty;
}
