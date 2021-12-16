/*
 * Copyright 2007-2020, CIIC Guanaitong, Co., Ltd.
 * All rights reserved.
 */

package com.ciicgat.circlefk.protocol.request.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class AdminUpdate {

    @ApiModelProperty(value = "管理员", required = true)
    @NotNull(message = "管理员Id不能为空")
    Long adminId;

    @ApiModelProperty(value = "姓名", required = true)
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "请选择状态")
    @Range(min = 1, max = 2, message = "状态为1或2")
    private Integer status;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
