/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50630
Source Host           : localhost:3306
Source Database       : sharding0

Target Server Type    : MYSQL
Target Server Version : 50630
File Encoding         : 65001

Date: 2017-05-17 14:35:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for loan_user_0
-- ----------------------------
DROP TABLE IF EXISTS `loan_user_0`;
CREATE TABLE `loan_user_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `mobile_phone` varchar(32) NOT NULL COMMENT '注册手机号',
  `password` varchar(128) DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(128) DEFAULT NULL COMMENT '盐值',
  `user_type` varchar(10) NOT NULL COMMENT '用户类型',
  `channel_type` varchar(10) NOT NULL COMMENT '来源渠道',
  `status` int(10) NOT NULL COMMENT '用户状态',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_code` (`code`),
  KEY `index_usermobile` (`mobile_phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户登录信息表';

-- ----------------------------
-- Table structure for loan_user_1
-- ----------------------------
DROP TABLE IF EXISTS `loan_user_1`;
CREATE TABLE `loan_user_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `mobile_phone` varchar(32) NOT NULL COMMENT '注册手机号',
  `password` varchar(128) DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(128) DEFAULT NULL COMMENT '盐值',
  `user_type` varchar(10) NOT NULL COMMENT '用户类型',
  `channel_type` varchar(10) NOT NULL COMMENT '来源渠道',
  `status` int(10) NOT NULL COMMENT '用户状态',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_code` (`code`),
  KEY `index_usermobile` (`mobile_phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录信息表';

-- ----------------------------
-- Table structure for loan_user_2
-- ----------------------------
DROP TABLE IF EXISTS `loan_user_2`;
CREATE TABLE `loan_user_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `mobile_phone` varchar(32) NOT NULL COMMENT '注册手机号',
  `password` varchar(128) DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(128) DEFAULT NULL COMMENT '盐值',
  `user_type` varchar(10) NOT NULL COMMENT '用户类型',
  `channel_type` varchar(10) NOT NULL COMMENT '来源渠道',
  `status` int(10) NOT NULL COMMENT '用户状态',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_code` (`code`),
  KEY `index_usermobile` (`mobile_phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录信息表';

-- ----------------------------
-- Table structure for user_operation_history_0
-- ----------------------------
DROP TABLE IF EXISTS `user_operation_history_0`;
CREATE TABLE `user_operation_history_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '注册手机号',
  `ip_address` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `client_id` varchar(64) DEFAULT NULL COMMENT '推送cilentId',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备唯一识别号',
  `device_version` varchar(20) DEFAULT NULL COMMENT '终端系统信息',
  `device_info` varchar(32) DEFAULT NULL COMMENT '终端设备信息',
  `source_code` varchar(10) DEFAULT NULL COMMENT '应用渠道号',
  `activity_code` varchar(10) DEFAULT NULL COMMENT '活动码(标识登录渠道信息)',
  `plateform` varchar(10) DEFAULT NULL COMMENT '平台类型',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei号码',
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi号码',
  `wifi_mac` varchar(64) DEFAULT NULL COMMENT 'wifiMac地址',
  `installed_list` text COMMENT '安装应用列表',
  `opt_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_usercode` (`user_code`),
  KEY `index_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=967 DEFAULT CHARSET=utf8 COMMENT='用户操作历史信息表';

-- ----------------------------
-- Table structure for user_operation_history_1
-- ----------------------------
DROP TABLE IF EXISTS `user_operation_history_1`;
CREATE TABLE `user_operation_history_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '注册手机号',
  `ip_address` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `client_id` varchar(64) DEFAULT NULL COMMENT '推送cilentId',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备唯一识别号',
  `device_version` varchar(20) DEFAULT NULL COMMENT '终端系统信息',
  `device_info` varchar(32) DEFAULT NULL COMMENT '终端设备信息',
  `source_code` varchar(10) DEFAULT NULL COMMENT '应用渠道号',
  `activity_code` varchar(10) DEFAULT NULL COMMENT '活动码(标识登录渠道信息)',
  `plateform` varchar(10) DEFAULT NULL COMMENT '平台类型',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei号码',
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi号码',
  `wifi_mac` varchar(64) DEFAULT NULL COMMENT 'wifiMac地址',
  `installed_list` text COMMENT '安装应用列表',
  `opt_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_usercode` (`user_code`),
  KEY `index_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=967 DEFAULT CHARSET=utf8 COMMENT='用户操作历史信息表';

DROP TABLE IF EXISTS `user_operation_history_2`;
CREATE TABLE `user_operation_history_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '注册手机号',
  `ip_address` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `client_id` varchar(64) DEFAULT NULL COMMENT '推送cilentId',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备唯一识别号',
  `device_version` varchar(20) DEFAULT NULL COMMENT '终端系统信息',
  `device_info` varchar(32) DEFAULT NULL COMMENT '终端设备信息',
  `source_code` varchar(10) DEFAULT NULL COMMENT '应用渠道号',
  `activity_code` varchar(10) DEFAULT NULL COMMENT '活动码(标识登录渠道信息)',
  `plateform` varchar(10) DEFAULT NULL COMMENT '平台类型',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei号码',
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi号码',
  `wifi_mac` varchar(64) DEFAULT NULL COMMENT 'wifiMac地址',
  `installed_list` text COMMENT '安装应用列表',
  `opt_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_usercode` (`user_code`),
  KEY `index_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作历史信息表';

-- ----------------------------
-- Table structure for user_operation_history_3
-- ----------------------------
DROP TABLE IF EXISTS `user_operation_history_3`;
CREATE TABLE `user_operation_history_3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '注册手机号',
  `ip_address` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `client_id` varchar(64) DEFAULT NULL COMMENT '推送cilentId',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备唯一识别号',
  `device_version` varchar(20) DEFAULT NULL COMMENT '终端系统信息',
  `device_info` varchar(32) DEFAULT NULL COMMENT '终端设备信息',
  `source_code` varchar(10) DEFAULT NULL COMMENT '应用渠道号',
  `activity_code` varchar(10) DEFAULT NULL COMMENT '活动码(标识登录渠道信息)',
  `plateform` varchar(10) DEFAULT NULL COMMENT '平台类型',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei号码',
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi号码',
  `wifi_mac` varchar(64) DEFAULT NULL COMMENT 'wifiMac地址',
  `installed_list` text COMMENT '安装应用列表',
  `opt_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_usercode` (`user_code`),
  KEY `index_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作历史信息表';

-- ----------------------------
-- Table structure for user_operation_history_201705
-- ----------------------------
DROP TABLE IF EXISTS `user_operation_history_201705`;
CREATE TABLE `user_operation_history_201705` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '注册手机号',
  `ip_address` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `client_id` varchar(64) DEFAULT NULL COMMENT '推送cilentId',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备唯一识别号',
  `device_version` varchar(20) DEFAULT NULL COMMENT '终端系统信息',
  `device_info` varchar(32) DEFAULT NULL COMMENT '终端设备信息',
  `source_code` varchar(10) DEFAULT NULL COMMENT '应用渠道号',
  `activity_code` varchar(10) DEFAULT NULL COMMENT '活动码(标识登录渠道信息)',
  `plateform` varchar(10) DEFAULT NULL COMMENT '平台类型',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei号码',
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi号码',
  `wifi_mac` varchar(64) DEFAULT NULL COMMENT 'wifiMac地址',
  `installed_list` text COMMENT '安装应用列表',
  `opt_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_usercode` (`user_code`),
  KEY `index_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户操作历史信息表';

-- ----------------------------
-- Table structure for user_operation_history_201706
-- ----------------------------
DROP TABLE IF EXISTS `user_operation_history_201706`;
CREATE TABLE `user_operation_history_201706` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(64) NOT NULL COMMENT 'code',
  `user_code` varchar(64) DEFAULT NULL COMMENT '用户code',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '注册手机号',
  `ip_address` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `client_id` varchar(64) DEFAULT NULL COMMENT '推送cilentId',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备唯一识别号',
  `device_version` varchar(20) DEFAULT NULL COMMENT '终端系统信息',
  `device_info` varchar(32) DEFAULT NULL COMMENT '终端设备信息',
  `source_code` varchar(10) DEFAULT NULL COMMENT '应用渠道号',
  `activity_code` varchar(10) DEFAULT NULL COMMENT '活动码(标识登录渠道信息)',
  `plateform` varchar(10) DEFAULT NULL COMMENT '平台类型',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei号码',
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi号码',
  `wifi_mac` varchar(64) DEFAULT NULL COMMENT 'wifiMac地址',
  `installed_list` text COMMENT '安装应用列表',
  `opt_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_id` (`id`),
  KEY `index_usercode` (`user_code`),
  KEY `index_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户操作历史信息表';

INSERT INTO `sharding_201705`.`user_operation_history_0` (`id`, `code`, `user_code`, `mobile_phone`, `ip_address`, `longitude`, `latitude`, `client_id`, `device_id`, `device_version`, `device_info`, `source_code`, `activity_code`, `plateform`, `imei`, `imsi`, `wifi_mac`, `installed_list`, `opt_type`, `del_flag`, `create_by`, `update_by`, `update_date`, `create_date`) VALUES ('1', '7e455c67abcd487d8a0ed85b3974217a', '9c2fe5ee-2993-11e7-b7b6-005056832de0', '18611478781', '10.0.12.254', '333.35353', '78.09876', 'clientId-51f422d8698f6d0e4c6a', 'deviceId-51f422d8698f6d0e4c6a', 'android 5.0', 'XiaoMi-2s', NULL, NULL, 'android', 'imei-5E2D5E2D5E2D5E2D5E2D5E2D', 'imsi-51f422d8698f6d0e4c6a', 'EDE2C1B0-2846-4022-9E1E-9F6B97919525', 'weixin,qq,yhjr,alipay', 'loginsms', '0', NULL, NULL, '2017-04-25 16:59:57', '2017-04-25 16:59:57');
INSERT INTO `sharding_201706`.`user_operation_history_1` (`id`, `code`, `user_code`, `mobile_phone`, `ip_address`, `longitude`, `latitude`, `client_id`, `device_id`, `device_version`, `device_info`, `source_code`, `activity_code`, `plateform`, `imei`, `imsi`, `wifi_mac`, `installed_list`, `opt_type`, `del_flag`, `create_by`, `update_by`, `update_date`, `create_date`) VALUES ('1', '7e455c67abcd487d8a0ed85b3974217a', '9c2fe5ee-2993-11e7-b7b6-005056832de0', '18611478781', '10.0.12.254', '333.35353', '78.09876', 'clientId-51f422d8698f6d0e4c6a', 'deviceId-51f422d8698f6d0e4c6a', 'android 5.0', 'XiaoMi-2s', NULL, NULL, 'android', 'imei-5E2D5E2D5E2D5E2D5E2D5E2D', 'imsi-51f422d8698f6d0e4c6a', 'EDE2C1B0-2846-4022-9E1E-9F6B97919525', 'weixin,qq,yhjr,alipay', 'loginsms', '0', NULL, NULL, '2017-04-25 16:59:57', '2017-04-25 16:59:57');


