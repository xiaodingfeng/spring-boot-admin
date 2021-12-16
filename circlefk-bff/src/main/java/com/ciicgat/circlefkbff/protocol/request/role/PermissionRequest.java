package com.ciicgat.circlefkbff.protocol.request.role;

import lombok.Data;

@Data
public class PermissionRequest {
    private String permissionid;
    private String permissionname;
    private Boolean checked;
}
