/*
 Navicat Premium Data Transfer

 Source Server         : 106.54.183.225
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 106.54.183.225:3306
 Source Schema         : dataplatform

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 31/03/2020 17:07:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apitest_category
-- ----------------------------
DROP TABLE IF EXISTS `apitest_category`;
CREATE TABLE `apitest_category` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(32) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_category
-- ----------------------------
BEGIN;
INSERT INTO `apitest_category` VALUES (1, '测试分类', NULL);
COMMIT;

-- ----------------------------
-- Table structure for apitest_collection
-- ----------------------------
DROP TABLE IF EXISTS `apitest_collection`;
CREATE TABLE `apitest_collection` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `collection_name` varchar(32) DEFAULT NULL,
  `wx_push` tinyint(1) NOT NULL DEFAULT '0',
  `creater_name` varchar(8) DEFAULT NULL,
  `creater_code` varchar(8) DEFAULT NULL,
  `excuter_name` varchar(8) DEFAULT NULL,
  `excuter_code` varchar(8) DEFAULT NULL,
  `excute_datetime` datetime DEFAULT NULL,
  `report_path` varchar(64) DEFAULT NULL,
  `thread_pool_size` int(4) NOT NULL DEFAULT '1' COMMENT '线程数',
  `repeat_times` int(4) NOT NULL DEFAULT '1' COMMENT '重复执行次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_collection
-- ----------------------------
BEGIN;
INSERT INTO `apitest_collection` VALUES (1, '集合demo', 0, '简单随风', 'admin', '简单随风', 'admin', '2020-03-31 11:21:38', '/report/1585624898946.html', 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for apitest_collection_api_order
-- ----------------------------
DROP TABLE IF EXISTS `apitest_collection_api_order`;
CREATE TABLE `apitest_collection_api_order` (
  `collection_id` int(4) NOT NULL COMMENT '集合id',
  `case_id` int(4) NOT NULL COMMENT 'caseid',
  `case_name` varchar(32) NOT NULL COMMENT 'case名称',
  `case_type` int(4) NOT NULL COMMENT 'case类型：1.http 2.dubbo 3.sql',
  KEY `collection_id` (`collection_id`) USING BTREE,
  KEY `api_id` (`case_id`) USING BTREE,
  CONSTRAINT `collection_id` FOREIGN KEY (`collection_id`) REFERENCES `apitest_collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_collection_api_order
-- ----------------------------
BEGIN;
INSERT INTO `apitest_collection_api_order` VALUES (1, 1, 'demo', 1);
COMMIT;

-- ----------------------------
-- Table structure for apitest_collection_excute_records
-- ----------------------------
DROP TABLE IF EXISTS `apitest_collection_excute_records`;
CREATE TABLE `apitest_collection_excute_records` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `collection_id` int(4) NOT NULL,
  `excuter_name` varchar(8) NOT NULL,
  `excuter_code` varchar(8) NOT NULL,
  `excute_datetime` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_collection_excute_records
-- ----------------------------
BEGIN;
INSERT INTO `apitest_collection_excute_records` VALUES (1, 1, '简单随风', 'admin', '2020-03-31 11:21:38');
COMMIT;

-- ----------------------------
-- Table structure for apitest_collection_focus
-- ----------------------------
DROP TABLE IF EXISTS `apitest_collection_focus`;
CREATE TABLE `apitest_collection_focus` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `collection_id` int(4) DEFAULT NULL,
  `user_code` varchar(8) DEFAULT NULL,
  `focus_date` date DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_collection_focus
-- ----------------------------
BEGIN;
INSERT INTO `apitest_collection_focus` VALUES (1, 1, 'admin', '2020-03-31');
COMMIT;

-- ----------------------------
-- Table structure for apitest_collection_variable
-- ----------------------------
DROP TABLE IF EXISTS `apitest_collection_variable`;
CREATE TABLE `apitest_collection_variable` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `collection_id` int(4) DEFAULT NULL,
  `variable_name` varchar(32) DEFAULT NULL,
  `variable_value` varchar(32) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for apitest_database_config
-- ----------------------------
DROP TABLE IF EXISTS `apitest_database_config`;
CREATE TABLE `apitest_database_config` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `db_name` varchar(16) NOT NULL,
  `url` varchar(64) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(16) NOT NULL,
  `database_type` int(1) NOT NULL COMMENT '1:mysql   2:oracle',
  `description` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for apitest_dubbo_case
-- ----------------------------
DROP TABLE IF EXISTS `apitest_dubbo_case`;
CREATE TABLE `apitest_dubbo_case` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `creater_name` varchar(8) NOT NULL COMMENT '创建人',
  `creater_code` varchar(8) NOT NULL COMMENT '创建人code',
  `create_date` date NOT NULL COMMENT '创建日期',
  `updater_name` varchar(8) DEFAULT NULL COMMENT '更新人',
  `updater_code` varchar(8) DEFAULT NULL COMMENT '更新人code',
  `update_date` date DEFAULT NULL COMMENT '更新日期',
  `system_name` varchar(16) DEFAULT NULL COMMENT '所属系统名称',
  `case_name` varchar(32) NOT NULL COMMENT '前端展示接口名称',
  `api_name` varchar(64) NOT NULL COMMENT '服务端请求所传name',
  `zk_address` varchar(128) NOT NULL COMMENT 'zookeeper地址',
  `service_name` varchar(128) NOT NULL COMMENT '服务名',
  `group_name` varchar(128) NOT NULL COMMENT '分组',
  `version` varchar(16) NOT NULL COMMENT '版本',
  `params` mediumtext COMMENT '入参',
  `function_name` varchar(64) NOT NULL DEFAULT '1' COMMENT '函数名',
  `class_name` varchar(128) NOT NULL COMMENT '类名',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `variable_list_value` text COMMENT '需要保存的变量',
  `expected_list_value` text COMMENT '需要校验的参数',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '最后一次执行状态',
  `result` text COMMENT '响应结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for apitest_global_variable
-- ----------------------------
DROP TABLE IF EXISTS `apitest_global_variable`;
CREATE TABLE `apitest_global_variable` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `variable_name` varchar(32) DEFAULT NULL,
  `variable_value` varchar(32) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_global_variable
-- ----------------------------
BEGIN;
INSERT INTO `apitest_global_variable` VALUES (1, 'VAR', '123', NULL);
COMMIT;

-- ----------------------------
-- Table structure for apitest_http_case
-- ----------------------------
DROP TABLE IF EXISTS `apitest_http_case`;
CREATE TABLE `apitest_http_case` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `creater_name` varchar(8) NOT NULL COMMENT '创建人',
  `creater_code` varchar(8) NOT NULL COMMENT '创建人code',
  `create_date` date NOT NULL COMMENT '创建日期',
  `updater_name` varchar(8) DEFAULT NULL COMMENT '更新人',
  `updater_code` varchar(8) DEFAULT NULL COMMENT '更新人code',
  `update_date` date DEFAULT NULL COMMENT '更新日期',
  `system_name` varchar(16) DEFAULT NULL COMMENT '所属系统名称',
  `case_name` varchar(32) NOT NULL COMMENT '接口名称',
  `http_type` int(1) NOT NULL COMMENT '1:http  2:https',
  `api_url` varchar(32) NOT NULL COMMENT '接口url',
  `api_port` varchar(32) DEFAULT NULL COMMENT '端口号',
  `api_method` varchar(8) NOT NULL COMMENT '请求方式',
  `api_path` varchar(128) DEFAULT NULL COMMENT '请求路径',
  `description` varchar(256) DEFAULT NULL COMMENT '备注',
  `header_value` text COMMENT '请求头',
  `body_type` int(2) NOT NULL DEFAULT '1' COMMENT 'body类型：1.json 2.urlform',
  `json_value` text COMMENT 'json的值',
  `form_value` text COMMENT 'urlform的值',
  `variable_list_value` text COMMENT '需要保存的变量',
  `expected_list_value` text COMMENT '需要校验的参数',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '最后一次执行状态',
  `result` text COMMENT '响应结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of apitest_http_case
-- ----------------------------
BEGIN;
INSERT INTO `apitest_http_case` VALUES (1, '简单随风', 'admin', '2020-03-31', NULL, NULL, NULL, '测试分类', 'demo', 1, 'www.baidu.com', NULL, 'GET', NULL, NULL, '[]', 1, NULL, '[]', '[]', '[]', 1, '接口响应异常');
COMMIT;

-- ----------------------------
-- Table structure for apitest_sql_case
-- ----------------------------
DROP TABLE IF EXISTS `apitest_sql_case`;
CREATE TABLE `apitest_sql_case` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `creater_name` varchar(8) NOT NULL COMMENT '创建人名',
  `creater_code` varchar(8) NOT NULL COMMENT '创建人工号',
  `create_date` date NOT NULL COMMENT '创建时间',
  `case_name` varchar(32) NOT NULL COMMENT 'case名',
  `database_id` int(4) NOT NULL COMMENT '数据库id',
  `sql` mediumtext COMMENT 'SQL语句',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `variable_list_value` text COMMENT '需要保存的变量',
  `result` text COMMENT '响应结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `code` varchar(8) DEFAULT NULL,
  `password` varchar(16) DEFAULT NULL,
  `name` varchar(16) DEFAULT NULL,
  `is_delete` int(4) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'admin', '123456', '简单随风', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
