/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50619
Source Host           : 127.0.0.1:3306
Source Database       : message

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-10-27 14:32:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `userId` varchar(32) NOT NULL,
  `msg` varchar(128) NOT NULL,
  `createTime` bigint(20) NOT NULL,
  `type` tinyint(1) NOT NULL,
  `receiverId` varchar(32) DEFAULT NULL,
  `typeId` int(11) NOT NULL,
  `appId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_pk` (`appId`,`typeId`,`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence` (
  `name` varchar(32) NOT NULL,
  `value` bigint(20) NOT NULL,
  `gmt_modified` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `nickName` varchar(16) DEFAULT NULL,
  `avatar` varchar(64) DEFAULT NULL,
  `lastLoginDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_unique_index` (`id`),
  KEY `un_unique_index` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
