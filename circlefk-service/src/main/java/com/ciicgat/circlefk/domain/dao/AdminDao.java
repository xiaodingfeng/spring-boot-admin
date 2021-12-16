package com.ciicgat.circlefk.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ciicgat.circlefk.domain.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDao extends BaseMapper<Admin> {
}
