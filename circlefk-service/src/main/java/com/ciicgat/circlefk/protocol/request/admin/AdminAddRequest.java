package com.ciicgat.circlefk.protocol.request.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AdminAddRequest {
    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "角色", required = true)
    @NotNull(message = "角色不能为空")
    private Long roleId;

    @ApiModelProperty(value = "姓名", required = true)
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "企业id", required = true)
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;
}
