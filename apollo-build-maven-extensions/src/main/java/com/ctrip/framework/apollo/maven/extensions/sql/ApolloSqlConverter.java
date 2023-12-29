/*
 * Copyright 2023 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.ctrip.framework.apollo.maven.extensions.sql;

import java.util.List;
import java.util.Map;

public class ApolloSqlConverter {

  public static void main(String[] args) {
    String moduleDir = args[0];
    String unixModuleDir = moduleDir.replace("\\", "/");
    String repositoryDir = ApolloSqlConverterUtil.replacePath(unixModuleDir,
        "/apollo-build-maven-extensions", "");

    Map<String, SqlTemplate> templates = ApolloSqlConverterUtil.getTemplates(repositoryDir);

    String srcDir = repositoryDir + "/scripts/sql-src";
    String targetParentDir = repositoryDir + "/scripts";

    convert(srcDir, targetParentDir, templates);
  }

  public static List<String> convert(String srcDir, String targetParentDir,
      Map<String, SqlTemplate> templates) {
    // '/scripts/sql-src/apolloconfigdb.sql'
    // '/scripts/sql-src/apolloportaldb.sql'
    // '/scripts/sql-src/delta/**/*.sql'
    List<String> srcSqlList = ApolloSqlConverterUtil.getSqlList(srcDir);

    // '/scripts/sql-src' -> '/scripts/sql'
    convertMainMysqlList(srcSqlList, srcDir, targetParentDir, templates);

    // '/scripts/sql-src' -> '/scripts/sql/assembly/mysql'
    convertAssemblyMysqlList(srcSqlList, srcDir, targetParentDir, templates);

    // '/scripts/sql-src' -> '/scripts/sql/assembly/h2'
    convertAssemblyH2List(srcSqlList, srcDir, targetParentDir, templates);

    return srcSqlList;
  }

  private static void convertMainMysqlList(List<String> srcSqlList,
      String srcDir, String targetParentDir, Map<String, SqlTemplate> templates) {
    String targetDir = targetParentDir + "/sql";
    for (String srcSql : srcSqlList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(srcSql, srcDir, targetDir);
      ApolloMainMysqlConverterUtil.convertMainMysql(srcSql, targetSql, templates);
    }
  }

  private static void convertAssemblyMysqlList(List<String> srcSqlList, String srcDir,
      String targetParentDir, Map<String, SqlTemplate> templates) {
    String targetDir = targetParentDir + "/sql/assembly/mysql";
    for (String srcSql : srcSqlList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(srcSql, srcDir, targetDir);
      ApolloAssemblyMysqlConverterUtil.convertAssemblyMysql(srcSql, targetSql, templates);
    }
  }

  private static void convertAssemblyH2List(List<String> srcSqlList,
      String srcDir, String targetParentDir, Map<String, SqlTemplate> templates) {
    String targetDir = targetParentDir + "/sql/assembly/h2";
    for (String srcSql : srcSqlList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(srcSql, srcDir, targetDir);
      ApolloAssemblyH2ConverterUtil.convertAssemblyH2(srcSql, targetSql, templates);
    }
  }
}
