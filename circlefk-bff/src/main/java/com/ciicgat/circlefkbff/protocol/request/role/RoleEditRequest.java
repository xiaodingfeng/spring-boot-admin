package com.ciicgat.circlefkbff.protocol.request.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
@Data
public class RoleEditRequest {

    @ApiModelProperty(value = "roleId", required = true)
    @NotNull(message = "roleId不能为空")
    private Long roleId;

    @ApiModelProperty(value = "名称", required = true)
    @NotNull(message = "名称不能为空")
    private String name;

    private String description;

    private List<PermissionGroupRequest> permissionGroup;
}