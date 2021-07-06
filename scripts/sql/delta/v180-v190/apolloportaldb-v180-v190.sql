--
-- Copyright 2021 Apollo Authors
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
# delta schema to upgrade apollo portal db from v1.8.0 to v1.9.0

Use ApolloPortalDB;

ALTER TABLE `App`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `AppNamespace`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `Consumer`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `ConsumerRole`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `ConsumerToken`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `Favorite`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `Permission`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `Role`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `RolePermission`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `ServerConfig`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `UserRole`
    MODIFY COLUMN `DataChange_CreatedBy` VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
    MODIFY COLUMN `DataChange_LastModifiedBy` VARCHAR(64) DEFAULT '' COMMENT '最后修改人邮箱前缀';

ALTER TABLE `Users`
    MODIFY COLUMN `Username` varchar(64) NOT NULL DEFAULT 'default' COMMENT '用户登录账户',
    ADD COLUMN `UserDisplayName` varchar(512) NOT NULL DEFAULT 'default' COMMENT '用户名称' AFTER `Password`;
UPDATE `Users` SET `UserDisplayName`=`Username` WHERE `UserDisplayName` = 'default';

ALTER TABLE `Users`
    MODIFY COLUMN `Password` varchar(512) NOT NULL DEFAULT 'default' COMMENT '密码';
UPDATE `Users` SET `Password` = REPLACE(`Password`, '{nonsensical}', '{placeholder}') WHERE `Password` LIKE '{nonsensical}%';
-- note: add the {bcrypt} prefix for `Users`.`Password` is not mandatory, and it may break the old version of apollo-portal while upgrading.
-- 注意: 向 `Users`.`Password` 添加 {bcrypt} 是非必须操作, 并且这个操作会导致升级 apollo-portal 集群的过程中旧版的 apollo-portal 无法使用.
-- UPDATE `Users` SET `Password` = CONCAT('{bcrypt}', `Password`) WHERE `Password` NOT LIKE '{%}%';
