package com.ciicgat.circlefk.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("Admin")
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String mobile;
    private String email;
    private Integer status;
    private Integer gender;
    private Long enterpriseId;
    private Integer loginTimes;
    private String timeLastLogin;
    private Integer ipLastLogin;
    private String timeCreated;
    private String timeModified;
}
