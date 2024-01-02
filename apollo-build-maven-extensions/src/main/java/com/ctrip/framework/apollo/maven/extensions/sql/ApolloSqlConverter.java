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

import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApolloSqlConverter {

  public static void main(String[] args) {
    String repositoryDir = ApolloSqlConverterUtil.getRepositoryDir();
    String targetParentDir = repositoryDir + "/scripts";

    convert(targetParentDir);
  }

  public static List<String> convert(String targetParentDir) {

    String repositoryDir = ApolloSqlConverterUtil.getRepositoryDir();
    String srcDir = repositoryDir + "/scripts/sql-src";

    Configuration configuration = createConfiguration(repositoryDir);

    SqlTemplateGist gists = ApolloSqlConverterUtil.getGists(repositoryDir);

    // '/scripts/sql-src/apolloconfigdb.sql'
    // '/scripts/sql-src/apolloportaldb.sql'
    // '/scripts/sql-src/delta/**/*.sql'
    List<String> srcSqlList = ApolloSqlConverterUtil.getSqlList(srcDir);
    List<SqlTemplate> templateList = ApolloSqlConverterUtil.toTemplates(srcSqlList, srcDir,
        configuration);

    // '/scripts/sql-src' -> '/scripts/sql'
    convertMainMysqlList(templateList, srcDir, targetParentDir, gists);

    // '/scripts/sql-src' -> '/scripts/sql/assembly/mysql'
    convertAssemblyMysqlList(templateList, srcDir, targetParentDir, gists);

    // '/scripts/sql-src' -> '/scripts/sql/assembly/mysql-without-database'
    convertAssemblyMysqlWithoutDatabaseList(templateList, srcDir, targetParentDir, gists);

    // '/scripts/sql-src' -> '/scripts/sql/assembly/h2'
    convertAssemblyH2List(templateList, srcDir, targetParentDir, gists);

    return srcSqlList;
  }

  private static Configuration createConfiguration(String repositoryDir) {
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
    try {
      configuration.setDirectoryForTemplateLoading(new File(repositoryDir + "/scripts/sql-src"));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
    return configuration;
  }

  private static void convertMainMysqlList(List<SqlTemplate> templateList,
      String srcDir, String targetParentDir, SqlTemplateGist gists) {
    String targetDir = targetParentDir + "/sql";

    SqlTemplateGist mainMysqlGists = SqlTemplateGist.builder()
        .autoGeneratedDeclaration(gists.getAutoGeneratedDeclaration())
        .h2Function("")
        .setupDatabase(gists.getSetupDatabase())
        .useDatabase(gists.getUseDatabase())
        .build();
    SqlTemplateContext context = SqlTemplateContext.builder()
        .gists(mainMysqlGists)
        .build();

    for (SqlTemplate sqlTemplate : templateList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(sqlTemplate.getSrcPath(), srcDir, targetDir);
      ApolloMainMysqlConverterUtil.convertMainMysql(sqlTemplate, targetSql, context);
    }
  }

  private static void convertAssemblyMysqlList(List<SqlTemplate> templateList, String srcDir,
      String targetParentDir, SqlTemplateGist gists) {
    String targetDir = targetParentDir + "/sql/assembly/mysql";

    SqlTemplateGist mainMysqlGists = SqlTemplateGist.builder()
        .autoGeneratedDeclaration(gists.getAutoGeneratedDeclaration())
        .h2Function("")
        .setupDatabase(gists.getSetupDatabase())
        .useDatabase(gists.getUseDatabase())
        .build();
    SqlTemplateContext context = SqlTemplateContext.builder()
        .gists(mainMysqlGists)
        .build();
    for (SqlTemplate sqlTemplate : templateList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(sqlTemplate.getSrcPath(), srcDir, targetDir);
      ApolloAssemblyMysqlConverterUtil.convertAssemblyMysql(sqlTemplate, targetSql, context);
    }
  }

  private static void convertAssemblyMysqlWithoutDatabaseList(List<SqlTemplate> templateList, String srcDir,
      String targetParentDir, SqlTemplateGist gists) {
    String targetDir = targetParentDir + "/sql/assembly/mysql-without-database";

    SqlTemplateGist mainMysqlGists = SqlTemplateGist.builder()
        .autoGeneratedDeclaration(gists.getAutoGeneratedDeclaration())
        .h2Function("")
        .setupDatabase("")
        .useDatabase("")
        .build();
    SqlTemplateContext context = SqlTemplateContext.builder()
        .gists(mainMysqlGists)
        .build();
    for (SqlTemplate sqlTemplate : templateList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(sqlTemplate.getSrcPath(), srcDir, targetDir);
      ApolloAssemblyMysqlConverterUtil.convertAssemblyMysql(sqlTemplate, targetSql, context);
    }
  }

  private static void convertAssemblyH2List(List<SqlTemplate> templateList, String srcDir,
      String targetParentDir, SqlTemplateGist gists) {
    String targetDir = targetParentDir + "/sql/assembly/h2";

    SqlTemplateGist mainMysqlGists = SqlTemplateGist.builder()
        .autoGeneratedDeclaration(gists.getAutoGeneratedDeclaration())
        .h2Function(gists.getH2Function())
        .setupDatabase("")
        .useDatabase("")
        .build();
    SqlTemplateContext context = SqlTemplateContext.builder()
        .gists(mainMysqlGists)
        .build();
    for (SqlTemplate sqlTemplate : templateList) {
      String targetSql = ApolloSqlConverterUtil.replacePath(sqlTemplate.getSrcPath(), srcDir, targetDir);
      ApolloAssemblyH2ConverterUtil.convertAssemblyH2(sqlTemplate, targetSql, context);
    }
  }
}
