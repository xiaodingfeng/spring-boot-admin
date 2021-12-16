package com.ciicgat.circlefkbff.protocol.request.role;

import lombok.Data;

import java.util.List;

@Data
public class PermissionGroupRequest {
    private String menuid;
    private String menuname;
    private Boolean menuVisible;

    private List<PermissionRequest> permissions;
}
