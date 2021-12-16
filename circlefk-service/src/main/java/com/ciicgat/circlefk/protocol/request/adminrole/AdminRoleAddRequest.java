package com.ciicgat.circlefk.protocol.request.adminrole;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class AdminRoleAddRequest {

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long adminId;

    @ApiModelProperty(value = "角色授权类型", required = true)
    @NotNull(message = "角色授权类型不能为空")
    private Integer roleType;

    @ApiModelProperty(value = "角色授权类型", required = true)
    @NotBlank(message = "角色授权类型不能为空")
    private String timeValidTo;
}
