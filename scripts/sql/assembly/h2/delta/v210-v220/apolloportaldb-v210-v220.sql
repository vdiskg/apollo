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
-- delta schema to upgrade apollo portal db from v2.1.0 to v2.2.0



ALTER TABLE `P_0_App`
    MODIFY COLUMN `AppId` VARCHAR(64) NOT NULL DEFAULT 'default' ;

ALTER TABLE `P_0_Consumer`
    MODIFY COLUMN `AppId` VARCHAR(64) NOT NULL DEFAULT 'default' ;

ALTER TABLE `P_0_Favorite`
    MODIFY COLUMN `AppId` VARCHAR(64) NOT NULL DEFAULT 'default' ;

ALTER TABLE `P_0_Favorite`
    DROP INDEX `AppId`,
    ADD INDEX `AppId` (`AppId`);



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
) ;




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
) ;
