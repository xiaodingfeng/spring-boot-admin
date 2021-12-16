-- ----------------------------
-- Table structure for Admin
-- ----------------------------
DROP TABLE IF EXISTS `Admin`;
CREATE TABLE `Admin`  (
  `id` bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮箱',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1、正常；2、禁用',
  `gender` tinyint(3) NOT NULL DEFAULT 1 COMMENT '性别',
  `enterpriseId` bigint(10) NOT NULL DEFAULT 0 COMMENT '企业Id',
  `loginTimes` int(11) NOT NULL DEFAULT 0 COMMENT '登录次数',
  `timeLastLogin` datetime(0) DEFAULT NULL COMMENT '上次登陆时间',
  `ipLastLogin` bigint(20) DEFAULT NULL COMMENT '上次登录ip',
  `timeCreated` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `timeModified` datetime(0) NOT NULL DEFAULT current_timestamp(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间' ,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_admin_username`(`username`) USING BTREE,
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'select * from Admin order by id desc limit 10;' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
