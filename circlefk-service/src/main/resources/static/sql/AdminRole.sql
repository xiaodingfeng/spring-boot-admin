-- ----------------------------
-- Table structure for AdminRole
-- ----------------------------
DROP TABLE IF EXISTS `AdminRole`;
CREATE TABLE `AdminRole`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `adminId` bigint(10) NOT NULL,
  `roleId` int(10) NOT NULL,
  `roleType` tinyint(4) NOT NULL DEFAULT 1 COMMENT '角色类型，1长期角色，2临时角色',
  `timeValidTo` date DEFAULT NULL COMMENT '角色过期时间',
  `timeCreated` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `timeModified` datetime(0) NOT NULL DEFAULT current_timestamp(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_adminrole_adminid`(`adminId`) USING BTREE,
  INDEX `idx_adminrole_roleId`(`roleId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
