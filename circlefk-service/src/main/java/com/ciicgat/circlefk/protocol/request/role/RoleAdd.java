package com.ciicgat.circlefk.protocol.request.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class RoleAdd {
    @ApiModelProperty(value = "名称", required = true)
    @NotNull(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "企业id", required = true)
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;

    private String description;
    private transient String permissions;
    private transient String permissionsText;
}
