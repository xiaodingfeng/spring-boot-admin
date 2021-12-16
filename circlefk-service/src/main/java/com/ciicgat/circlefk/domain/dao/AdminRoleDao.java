package com.ciicgat.circlefk.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ciicgat.circlefk.domain.entity.AdminRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRoleDao extends BaseMapper<AdminRole>  {
    List<AdminRole> getValidAdminRoles(@Param("adminId") Long adminId);
}
