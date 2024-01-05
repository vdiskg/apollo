--
-- Copyright 2023 Apollo Authors
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 
-- ===============================================================================
-- ==                                                                           ==
-- ==                     Generated from 'scripts/sql-src/'                     ==
-- == by running 'mvn compile -pl apollo-build-sql-converter -Psql-converter'. ==
-- ==                              DO NOT EDIT !!!                              ==
-- ==                                                                           ==
-- ===============================================================================
-- 

-- H2 Function
-- ------------------------------------------------------------
CREATE ALIAS IF NOT EXISTS UNIX_TIMESTAMP FOR "com.ctrip.framework.apollo.common.jpa.H2Function.unixTimestamp";

-- 

-- Dump of table app
-- ------------------------------------------------------------



CREATE TABLE `P_0_App` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `Name` varchar(500) NOT NULL DEFAULT 'default' ,
  `OrgId` varchar(32) NOT NULL DEFAULT 'default' ,
  `OrgName` varchar(64) NOT NULL DEFAULT 'default' ,
  `OwnerName` varchar(500) NOT NULL DEFAULT 'default' ,
  `OwnerEmail` varchar(500) NOT NULL DEFAULT 'default' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`DeletedAt`),
  KEY (`DataChange_LastTime`),
  KEY (`Name`)
)   ;



-- Dump of table appnamespace
-- ------------------------------------------------------------



CREATE TABLE `P_0_AppNamespace` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `Name` varchar(32) NOT NULL DEFAULT '' ,
  `AppId` varchar(64) NOT NULL DEFAULT '' ,
  `Format` varchar(32) NOT NULL DEFAULT 'properties' ,
  `IsPublic` boolean NOT NULL DEFAULT FALSE ,
  `Comment` varchar(64) NOT NULL DEFAULT '' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`Name`,`DeletedAt`),
  KEY (`Name`,`AppId`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table consumer
-- ------------------------------------------------------------



CREATE TABLE `P_0_Consumer` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `Name` varchar(500) NOT NULL DEFAULT 'default' ,
  `OrgId` varchar(32) NOT NULL DEFAULT 'default' ,
  `OrgName` varchar(64) NOT NULL DEFAULT 'default' ,
  `OwnerName` varchar(500) NOT NULL DEFAULT 'default' ,
  `OwnerEmail` varchar(500) NOT NULL DEFAULT 'default' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table consumeraudit
-- ------------------------------------------------------------



CREATE TABLE `P_0_ConsumerAudit` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `ConsumerId` int(11) unsigned DEFAULT NULL ,
  `Uri` varchar(1024) NOT NULL DEFAULT '' ,
  `Method` varchar(16) NOT NULL DEFAULT '' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`DataChange_LastTime`),
  KEY (`ConsumerId`)
)   ;



-- Dump of table consumerrole
-- ------------------------------------------------------------



CREATE TABLE `P_0_ConsumerRole` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `ConsumerId` int(11) unsigned DEFAULT NULL ,
  `RoleId` int(10) unsigned DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`ConsumerId`,`RoleId`,`DeletedAt`),
  KEY (`DataChange_LastTime`),
  KEY (`RoleId`)
)   ;



-- Dump of table consumertoken
-- ------------------------------------------------------------



CREATE TABLE `P_0_ConsumerToken` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `ConsumerId` int(11) unsigned DEFAULT NULL ,
  `Token` varchar(128) NOT NULL DEFAULT '' ,
  `Expires` datetime NOT NULL DEFAULT '2099-01-01 00:00:00' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`Token`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;

-- Dump of table favorite
-- ------------------------------------------------------------



CREATE TABLE `P_0_Favorite` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `UserId` varchar(32) NOT NULL DEFAULT 'default' ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `Position` int(32) NOT NULL DEFAULT '10000' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`UserId`,`AppId`,`DeletedAt`),
  KEY (`AppId`),
  KEY (`DataChange_LastTime`)
)  AUTO_INCREMENT=23  ;

-- Dump of table permission
-- ------------------------------------------------------------



CREATE TABLE `P_0_Permission` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `PermissionType` varchar(32) NOT NULL DEFAULT '' ,
  `TargetId` varchar(256) NOT NULL DEFAULT '' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`TargetId`,`PermissionType`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table role
-- ------------------------------------------------------------



CREATE TABLE `P_0_Role` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `RoleName` varchar(256) NOT NULL DEFAULT '' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`RoleName`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table rolepermission
-- ------------------------------------------------------------



CREATE TABLE `P_0_RolePermission` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `RoleId` int(10) unsigned DEFAULT NULL ,
  `PermissionId` int(10) unsigned DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`RoleId`,`PermissionId`,`DeletedAt`),
  KEY (`DataChange_LastTime`),
  KEY (`PermissionId`)
)   ;



-- Dump of table serverconfig
-- ------------------------------------------------------------



CREATE TABLE `P_0_ServerConfig` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `Key` varchar(64) NOT NULL DEFAULT 'default' ,
  `Value` varchar(2048) NOT NULL DEFAULT 'default' ,
  `Comment` varchar(1024) DEFAULT '' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`Key`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table userrole
-- ------------------------------------------------------------



CREATE TABLE `P_0_UserRole` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `UserId` varchar(128) DEFAULT '' ,
  `RoleId` int(10) unsigned DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`UserId`,`RoleId`,`DeletedAt`),
  KEY (`DataChange_LastTime`),
  KEY (`RoleId`)
)   ;

-- Dump of table Users
-- ------------------------------------------------------------



CREATE TABLE `P_0_Users` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `Username` varchar(64) NOT NULL DEFAULT 'default' ,
  `Password` varchar(512) NOT NULL DEFAULT 'default' ,
  `UserDisplayName` varchar(512) NOT NULL DEFAULT 'default' ,
  `Email` varchar(64) NOT NULL DEFAULT 'default' ,
  `Enabled` tinyint(4) DEFAULT NULL ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`Username`)
)   ;


-- Dump of table Authorities
-- ------------------------------------------------------------



CREATE TABLE `P_0_Authorities` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `Username` varchar(64) NOT NULL,
  `Authority` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`)
)  ;

-- spring session (https://github.com/spring-projects/spring-session/blob/faee8f1bdb8822a5653a81eba838dddf224d92d6/spring-session-jdbc/src/main/resources/org/springframework/session/jdbc/schema-mysql.sql)
-- Dump of table SPRING_SESSION
-- ------------------------------------------------------------



CREATE TABLE `P_0_SPRING_SESSION` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY (`SESSION_ID`),
  KEY (`EXPIRY_TIME`),
  KEY (`PRINCIPAL_NAME`)
)   ;

-- Dump of table SPRING_SESSION_ATTRIBUTES
-- ------------------------------------------------------------



CREATE TABLE `P_0_SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `P_0_SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
)   ;

-- Dump of table AuditLog
-- ------------------------------------------------------------



CREATE TABLE `P_0_AuditLog` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `TraceId` varchar(32) NOT NULL DEFAULT '' ,
  `SpanId` varchar(32) NOT NULL DEFAULT '' ,
  `ParentSpanId` varchar(32) DEFAULT NULL ,
  `FollowsFromSpanId` varchar(32) DEFAULT NULL ,
  `Operator` varchar(64) NOT NULL DEFAULT 'anonymous' ,
  `OpType` varchar(50) NOT NULL DEFAULT 'default' ,
  `OpName` varchar(150) NOT NULL DEFAULT 'default' ,
  `Description` varchar(200) DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) DEFAULT NULL ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`TraceId`),
  KEY (`OpName`),
  KEY (`DataChange_CreatedTime`),
  KEY (`Operator`)
)   ;

-- Dump of table AuditLogDataInfluence
-- ------------------------------------------------------------



CREATE TABLE `P_0_AuditLogDataInfluence` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `SpanId` char(32) NOT NULL DEFAULT '' ,
  `InfluenceEntityId` varchar(50) NOT NULL DEFAULT '0' ,
  `InfluenceEntityName` varchar(50) NOT NULL DEFAULT 'default' ,
  `FieldName` varchar(50) DEFAULT NULL ,
  `FieldOldValue` varchar(500) DEFAULT NULL ,
  `FieldNewValue` varchar(500) DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) DEFAULT NULL ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`SpanId`),
  KEY (`DataChange_CreatedTime`),
  KEY (`InfluenceEntityId`)
)   ;

-- Config
-- ------------------------------------------------------------
INSERT INTO `P_0_ServerConfig` (`Key`, `Value`, `Comment`)
VALUES
    ('apollo.portal.envs', 'dev', '可支持的环境列表'),
    ('organizations', '[{"orgId":"TEST1","orgName":"样例部门1"},{"orgId":"TEST2","orgName":"样例部门2"}]', '部门列表'),
    ('superAdmin', 'apollo', 'Portal超级管理员'),
    ('api.readTimeout', '10000', 'http接口read timeout'),
    ('consumer.token.salt', 'someSalt', 'consumer token salt'),
    ('admin.createPrivateNamespace.switch', 'true', '是否允许项目管理员创建私有namespace'),
    ('configView.memberOnly.envs', 'pro', '只对项目成员显示配置信息的环境列表，多个env以英文逗号分隔'),
    ('apollo.portal.meta.servers', '{}', '各环境Meta Service列表');


INSERT INTO `P_0_Users` (`Username`, `Password`, `UserDisplayName`, `Email`, `Enabled`)
VALUES
	('apollo', '$2a$10$7r20uS.BQ9uBpf3Baj3uQOZvMVvB1RN3PYoKE94gtz2.WAOuiiwXS', 'apollo', 'apollo@acme.com', 1);

INSERT INTO `P_0_Authorities` (`Username`, `Authority`) VALUES ('apollo', 'ROLE_user');

-- 
-- ===============================================================================
-- ==                                                                           ==
-- ==                     Generated from 'scripts/sql-src/'                     ==
-- == by running 'mvn compile -pl apollo-build-sql-converter -Psql-converter'. ==
-- ==                              DO NOT EDIT !!!                              ==
-- ==                                                                           ==
-- ===============================================================================

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
