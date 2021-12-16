package com.ciicgat.circlefkbff.feign.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
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
