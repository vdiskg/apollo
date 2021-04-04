Use ApolloPortalDB;

ALTER TABLE `Users`
    ADD COLUMN `PreferredUsername` varchar(512) NOT NULL DEFAULT 'default' COMMENT '用户名称' AFTER `Password`;
UPDATE `Users` SET `PreferredUsername`=`Username` WHERE `PreferredUsername` = 'default';
