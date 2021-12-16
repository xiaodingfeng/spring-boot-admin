package com.ciicgat.circlefkbff;

import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;
import com.ciicgat.circlefkbff.protocol.request.role.RoleAddRequest;
import com.ciicgat.circlefkbff.service.auth.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTest {

    @Resource
    private RoleService roleService;

    @Test
    public void add() {
        RoleAddRequest parse = JSON.parse("{\"name\":\"灌灌灌灌\",\"enterpriseId\":1,\"appId\":\"10000583\",\"description\":\"阿迪达斯\",\"permissionGroup\":[{\"menuid\":\"position\",\"menuname\":\"岗位管理\",\"permissionList\":[{\"permissionid\":\"position_select\",\"permissionname\":\"岗位管理-查看\",\"checked\":true},{\"permissionid\":\"position_insert\",\"permissionname\":\"岗位管理-新建岗位\",\"checked\":true},{\"permissionid\":\"position_update\",\"permissionname\":\"岗位管理-修改岗位\",\"checked\":true},{\"permissionid\":\"set_admin_position\",\"permissionname\":\"岗位管理-授权员工岗位\",\"checked\":true},{\"permissionid\":\"cancel_admin_position\",\"permissionname\":\"岗位管理-取消员工岗位授权\",\"checked\":true},{\"permissionid\":\"setting_position_roles\",\"permissionname\":\"岗位管理-配置关联角色\",\"checked\":true}],\"menuVisible\":true},{\"menuid\":\"user\",\"menuname\":\"员工管理\",\"permissionList\":[{\"permissionid\":\"user_select\",\"permissionname\":\"员工管理-查看\",\"checked\":true},{\"permissionid\":\"user_setting\",\"permissionname\":\"员工管理-设置\",\"checked\":true},{\"permissionid\":\"user_reset_password\",\"permissionname\":\"员工管理-重置密码\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"role\",\"menuname\":\"角色管理\",\"permissionList\":[{\"permissionid\":\"role_select\",\"permissionname\":\"角色管理-查看\",\"checked\":true},{\"permissionid\":\"role_setting\",\"permissionname\":\"角色管理-设置\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"log\",\"menuname\":\"日志查看\",\"permissionList\":[{\"permissionid\":\"log_select\",\"permissionname\":\"日志-查看\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"homepage\",\"menuname\":\"首页设置\",\"permissionList\":[{\"permissionid\":\"homepage_select\",\"permissionname\":\"首页设置-查看\",\"checked\":true},{\"permissionid\":\"homepage_setting\",\"permissionname\":\"首页设置-设置\",\"checked\":true}],\"menuVisible\":true},{\"menuid\":\"ipwhitelist\",\"menuname\":\"IP白名单\",\"permissionList\":[{\"permissionid\":\"ipwhitelist_select\",\"permissionname\":\"IP白名单-查看\",\"checked\":true},{\"permissionid\":\"ipwhitelist_setting\",\"permissionname\":\"IP白名单-设置\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"helper\",\"menuname\":\"帮助中心\",\"permissionList\":[{\"permissionid\":\"helper_list\",\"permissionname\":\"帮助中心-查看\",\"checked\":true},{\"permissionid\":\"helper_setting\",\"permissionname\":\"帮助中心-操作\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"notice\",\"menuname\":\"公告设置\",\"permissionList\":[{\"permissionid\":\"notice_setting\",\"permissionname\":\"公告设置-设置\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"datamoduleregister\",\"menuname\":\"数据模块\",\"permissionList\":[{\"permissionid\":\"datamoduleregister_setting\",\"permissionname\":\"数据模块-设置\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"datagroup\",\"menuname\":\"数据权限组\",\"permissionList\":[{\"permissionid\":\"datagroup_setting\",\"permissionname\":\"数据权限组-设置\",\"checked\":true}],\"menuVisible\":false},{\"menuid\":\"dataattribute\",\"menuname\":\"数据权限组属性\",\"permissionList\":[{\"permissionid\":\"dataattribute_setting\",\"permissionname\":\"数据权限组属性-设置\",\"checked\":true}],\"menuVisible\":false}]}", RoleAddRequest.class);
        System.out.println(roleService.addRoleInfo(parse));
    }

    @Test
    public void get() {
        System.out.println(JSON.toJSONString(roleService.get(1L)));
    }

    @Test
    public void getRoleListByEnterpriseId() {
        System.out.println(JSON.toJSONString(roleService.getRoleListByEnterpriseId(10000583L)));
    }
}
