package com.ciicgat.circlefk.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ciicgat.circlefk.protocol.response.auth.Permission;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("Role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long enterpriseId;
    private String name;
    private String description;
    private String permissions;
    private String permissionsText;
    private String timeCreated;
    private String timeModified;

    @TableField(exist = false)
    private Permission permissionEntity;

    @TableField(exist = false)
    private Permission permissionDisplay;

    @TableField(exist = false)
    private Long authQty;
}
