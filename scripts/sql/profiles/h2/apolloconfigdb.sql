--
-- Copyright 2024 Apollo Authors
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



CREATE TABLE `C_0_App` (
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



CREATE TABLE `C_0_AppNamespace` (
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



-- Dump of table audit
-- ------------------------------------------------------------



CREATE TABLE `C_0_Audit` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `EntityName` varchar(50) NOT NULL DEFAULT 'default' ,
  `EntityId` int(10) unsigned DEFAULT NULL ,
  `OpName` varchar(50) NOT NULL DEFAULT 'default' ,
  `Comment` varchar(500) DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table cluster
-- ------------------------------------------------------------



CREATE TABLE `C_0_Cluster` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `Name` varchar(32) NOT NULL DEFAULT '' ,
  `AppId` varchar(64) NOT NULL DEFAULT '' ,
  `ParentClusterId` int(10) unsigned NOT NULL DEFAULT '0' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`Name`,`DeletedAt`),
  KEY (`ParentClusterId`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table commit
-- ------------------------------------------------------------



CREATE TABLE `C_0_Commit` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `ChangeSets` longtext NOT NULL ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ClusterName` varchar(500) NOT NULL DEFAULT 'default' ,
  `NamespaceName` varchar(500) NOT NULL DEFAULT 'default' ,
  `Comment` varchar(500) DEFAULT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`DataChange_LastTime`),
  KEY (`AppId`),
  KEY (`ClusterName`),
  KEY (`NamespaceName`)
)   ;

-- Dump of table grayreleaserule
-- ------------------------------------------------------------



CREATE TABLE `C_0_GrayReleaseRule` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ClusterName` varchar(32) NOT NULL DEFAULT 'default' ,
  `NamespaceName` varchar(32) NOT NULL DEFAULT 'default' ,
  `BranchName` varchar(32) NOT NULL DEFAULT 'default' ,
  `Rules` varchar(16000) DEFAULT '[]' ,
  `ReleaseId` int(11) unsigned NOT NULL DEFAULT '0' ,
  `BranchStatus` tinyint(2) DEFAULT '1' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`DataChange_LastTime`),
  KEY (`AppId`,`ClusterName`,`NamespaceName`)
)   ;


-- Dump of table instance
-- ------------------------------------------------------------



CREATE TABLE `C_0_Instance` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ClusterName` varchar(32) NOT NULL DEFAULT 'default' ,
  `DataCenter` varchar(64) NOT NULL DEFAULT 'default' ,
  `Ip` varchar(32) NOT NULL DEFAULT '' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`ClusterName`,`Ip`,`DataCenter`),
  KEY (`Ip`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table instanceconfig
-- ------------------------------------------------------------



CREATE TABLE `C_0_InstanceConfig` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `InstanceId` int(11) unsigned DEFAULT NULL ,
  `ConfigAppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ConfigClusterName` varchar(32) NOT NULL DEFAULT 'default' ,
  `ConfigNamespaceName` varchar(32) NOT NULL DEFAULT 'default' ,
  `ReleaseKey` varchar(64) NOT NULL DEFAULT '' ,
  `ReleaseDeliveryTime` timestamp NULL DEFAULT NULL ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`InstanceId`,`ConfigAppId`,`ConfigNamespaceName`),
  KEY (`ReleaseKey`),
  KEY (`DataChange_LastTime`),
  KEY (`ConfigAppId`,`ConfigClusterName`,`ConfigNamespaceName`,`DataChange_LastTime`)
)   ;



-- Dump of table item
-- ------------------------------------------------------------



CREATE TABLE `C_0_Item` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `NamespaceId` int(10) unsigned NOT NULL DEFAULT '0' ,
  `Key` varchar(128) NOT NULL DEFAULT 'default' ,
  `Type` tinyint(3) unsigned NOT NULL DEFAULT '0' ,
  `Value` longtext NOT NULL ,
  `Comment` varchar(1024) DEFAULT '' ,
  `LineNum` int(10) unsigned DEFAULT '0' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`NamespaceId`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table namespace
-- ------------------------------------------------------------



CREATE TABLE `C_0_Namespace` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ClusterName` varchar(500) NOT NULL DEFAULT 'default' ,
  `NamespaceName` varchar(500) NOT NULL DEFAULT 'default' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`ClusterName`,`NamespaceName`,`DeletedAt`),
  KEY (`DataChange_LastTime`),
  KEY (`NamespaceName`)
)   ;



-- Dump of table namespacelock
-- ------------------------------------------------------------



CREATE TABLE `C_0_NamespaceLock` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `NamespaceId` int(10) unsigned NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `IsDeleted` boolean DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`NamespaceId`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;



-- Dump of table release
-- ------------------------------------------------------------



CREATE TABLE `C_0_Release` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `ReleaseKey` varchar(64) NOT NULL DEFAULT '' ,
  `Name` varchar(64) NOT NULL DEFAULT 'default' ,
  `Comment` varchar(256) DEFAULT NULL ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ClusterName` varchar(500) NOT NULL DEFAULT 'default' ,
  `NamespaceName` varchar(500) NOT NULL DEFAULT 'default' ,
  `Configurations` longtext NOT NULL ,
  `IsAbandoned` boolean NOT NULL DEFAULT FALSE ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`ReleaseKey`,`DeletedAt`),
  KEY (`AppId`,`ClusterName`,`NamespaceName`),
  KEY (`DataChange_LastTime`)
)   ;


-- Dump of table releasehistory
-- ------------------------------------------------------------



CREATE TABLE `C_0_ReleaseHistory` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `ClusterName` varchar(32) NOT NULL DEFAULT 'default' ,
  `NamespaceName` varchar(32) NOT NULL DEFAULT 'default' ,
  `BranchName` varchar(32) NOT NULL DEFAULT 'default' ,
  `ReleaseId` int(11) unsigned NOT NULL DEFAULT '0' ,
  `PreviousReleaseId` int(11) unsigned NOT NULL DEFAULT '0' ,
  `Operation` tinyint(3) unsigned NOT NULL DEFAULT '0' ,
  `OperationContext` longtext NOT NULL ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`AppId`,`ClusterName`,`NamespaceName`,`BranchName`),
  KEY (`ReleaseId`),
  KEY (`DataChange_LastTime`),
  KEY (`PreviousReleaseId`)
)   ;


-- Dump of table releasemessage
-- ------------------------------------------------------------



CREATE TABLE `C_0_ReleaseMessage` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `Message` varchar(1024) NOT NULL DEFAULT '' ,
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  KEY (`DataChange_LastTime`),
  KEY (`Message`)
)   ;



-- Dump of table serverconfig
-- ------------------------------------------------------------



CREATE TABLE `C_0_ServerConfig` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `Key` varchar(64) NOT NULL DEFAULT 'default' ,
  `Cluster` varchar(32) NOT NULL DEFAULT 'default' ,
  `Value` varchar(2048) NOT NULL DEFAULT 'default' ,
  `Comment` varchar(1024) DEFAULT '' ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`Key`,`Cluster`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;

-- Dump of table accesskey
-- ------------------------------------------------------------



CREATE TABLE `C_0_AccessKey` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `AppId` varchar(64) NOT NULL DEFAULT 'default' ,
  `Secret` varchar(128) NOT NULL DEFAULT '' ,
  `IsEnabled` boolean NOT NULL DEFAULT FALSE ,
  `IsDeleted` boolean NOT NULL DEFAULT FALSE ,
  `DeletedAt` BIGINT(20) NOT NULL DEFAULT '0' ,
  `DataChange_CreatedBy` varchar(64) NOT NULL DEFAULT 'default' ,
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastModifiedBy` varchar(64) DEFAULT '' ,
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE KEY (`AppId`,`Secret`,`DeletedAt`),
  KEY (`DataChange_LastTime`)
)   ;


-- Dump of table serviceregistry
-- ------------------------------------------------------------



CREATE TABLE `C_0_ServiceRegistry` (
  `Id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `ServiceName` VARCHAR(64) NOT NULL ,
  `Uri` VARCHAR(64) NOT NULL ,
  `Cluster` VARCHAR(64) NOT NULL ,
  `Metadata` VARCHAR(1024) NOT NULL DEFAULT '{}' ,
  `DataChange_CreatedTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `DataChange_LastTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IX_UNIQUE_KEY` (`ServiceName`, `Uri`),
  INDEX `IX_DataChange_LastTime` (`DataChange_LastTime`)
)   ;

-- Dump of table AuditLog
-- ------------------------------------------------------------



CREATE TABLE `C_0_AuditLog` (
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



CREATE TABLE `C_0_AuditLogDataInfluence` (
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
INSERT INTO `C_0_ServerConfig` (`Key`, `Cluster`, `Value`, `Comment`)
VALUES
    ('eureka.service.url', 'default', 'http://localhost:8080/eureka/', 'Eureka服务Url，多个service以英文逗号分隔'),
    ('namespace.lock.switch', 'default', 'false', '一次发布只能有一个人修改开关'),
    ('item.key.length.limit', 'default', '128', 'item key 最大长度限制'),
    ('item.value.length.limit', 'default', '20000', 'item value最大长度限制'),
    ('config-service.cache.enabled', 'default', 'false', 'ConfigService是否开启缓存，开启后能提高性能，但是会增大内存消耗！');

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
