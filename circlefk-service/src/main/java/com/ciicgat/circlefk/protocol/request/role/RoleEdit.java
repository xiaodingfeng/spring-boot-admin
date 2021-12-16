package com.ciicgat.circlefk.protocol.request.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class RoleEdit {
    @ApiModelProperty(value = "roleId", required = true)
    @NotNull(message = "roleId不能为空")
    private long id;

    @ApiModelProperty(value = "名称", required = true)
    @NotNull(message = "名称不能为空")
    private String name;

    private String description;
    private transient String permissions;
    private transient String permissionsText;
}
