package com.ciicgat.circlefk.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("AdminRole")
public class AdminRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long adminId;
    private Long roleId;
    private Integer roleType;
    private String timeValidTo;
    private String timeCreated;
    private String timeModified;
}
