-- ----------------------------
-- Table structure for Role
-- ----------------------------
DROP TABLE IF EXISTS `Role`;
CREATE TABLE `Role`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `enterpriseId` bigint(10) NOT NULL DEFAULT 0 COMMENT '企业Id',
  `name` varchar(20) NOT NULL DEFAULT '',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1、正常；2、禁用',
  `description` varchar(255) NULL DEFAULT '',
  `permissions` text NOT NULL DEFAULT '',
  `permissionsText` text NOT NULL DEFAULT '',
  `timeCreated` datetime(0)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `timeModified` datetime(0) DEFAULT current_timestamp(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
