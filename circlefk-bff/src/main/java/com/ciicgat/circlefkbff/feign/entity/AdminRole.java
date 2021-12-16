package com.ciicgat.circlefkbff.feign.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long adminId;
    private Long roleId;
    private Integer roleType;
    private String timeValidTo;
    private String timeCreated;
    private String timeModified;
}
