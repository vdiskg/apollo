/*
 * Copyright 2024 Apollo Authors
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
package com.ctrip.framework.apollo.build.sql.converter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApolloH2ConverterUtil {

  private static final Pattern CREATE_TABLE_PATTERN = Pattern.compile(
      "CREATE\\s+TABLE\\s+(`)?(?<tableName>[a-zA-Z0-9\\-_]+)(`)?", Pattern.CASE_INSENSITIVE);

  private static final Pattern ALTER_TABLE_PATTERN = Pattern.compile(
      "ALTER\\s+TABLE\\s+(`)?(?<tableName>[a-zA-Z0-9\\-_]+)(`)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADD_COLUMN_PATTERN = Pattern.compile(
      "ADD\\s+COLUMN\\s+(`)?(?<columnName>[a-zA-Z0-9\\-_]+)(`)?(?<subStatement>.*)[,;]",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern MODIFY_COLUMN_PATTERN = Pattern.compile(
      "MODIFY\\s+COLUMN\\s+(`)?(?<columnName>[a-zA-Z0-9\\-_]+)(`)?(?<subStatement>.*)[,;]",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern CHANGE_PATTERN = Pattern.compile(
      "CHANGE\\s+(`)?(?<oldColumnName>[a-zA-Z0-9\\-_]+)(`)?\\s+(`)?(?<newColumnName>[a-zA-Z0-9\\-_]+)(`)?(?<subStatement>.*)[,;]",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern DROP_COLUMN_PATTERN = Pattern.compile(
      "DROP\\s+(COLUMN\\s+)?(`)?(?<columnName>[a-zA-Z0-9\\-_]+)(`)?\\s*[,;]",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern ADD_KEY_PATTERN = Pattern.compile(
      "ADD\\s+(?<indexType>(UNIQUE\\s+)?KEY)\\s+(`)?(?<indexName>[a-zA-Z0-9\\-_]+)(`)?(?<subStatement>.*)[,;]",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern ADD_INDEX_PATTERN = Pattern.compile(
      "ADD\\s+(?<indexType>(UNIQUE\\s+)?INDEX)\\s+(`)?(?<indexName>[a-zA-Z0-9\\-_]+)(`)?(?<subStatement>.*)[,;]",
      Pattern.CASE_INSENSITIVE);

  private static final Pattern CREATE_INDEX_ON_PATTERN = Pattern.compile(
      "CREATE\\s+INDEX\\s+(`)?(?<indexName>[a-zA-Z0-9\\-_]+)(`)?\\s+ON\\s+(`)?(?<tableName>[a-zA-Z0-9\\-_]+)(`)?",
      Pattern.CASE_INSENSITIVE);

  private static final Pattern INDEX_NAME_PATTERN = Pattern.compile(
      "KEY\\s*(`)?(?<indexName>[a-zA-Z0-9\\-_]+)(`)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern DROP_INDEX_PATTERN = Pattern.compile(
      "DROP\\s+INDEX\\s+(`)?(?<indexName>[a-zA-Z0-9\\-_]+)(`)?\\s*[,;]", Pattern.CASE_INSENSITIVE);
  private static final Pattern CREATE_INDEX_PATTERN = Pattern.compile(
      "CREATE\\s+(?<indexType>(UNIQUE\\s+)?INDEX)\\s+(`)?(?<indexName>[a-zA-Z0-9\\-_]+)(`)?",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern PREFIX_INDEX_PATTERN = Pattern.compile(
      "(?<prefix>\\("
          + "((`)?[a-zA-Z0-9\\-_]+(`)?\\s*(\\([0-9]+\\))?,)*)"
          + "(`)?(?<columnName>[a-zA-Z0-9\\-_]+)(`)?\\s*\\([0-9]+\\)"
          + "(?<suffix>(,(`)?[a-zA-Z0-9\\-_]+(`)?\\s*(\\([0-9]+\\))?)*"
          + "\\))");

  public static void convert(SqlTemplate sqlTemplate, String targetSql,
      SqlTemplateContext context) {

    ApolloSqlConverterUtil.ensureDirectories(targetSql);

    String rawText = ApolloSqlConverterUtil.process(sqlTemplate, context);

    List<SqlStatement> sqlStatements = ApolloSqlConverterUtil.toStatements(rawText);
    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(targetSql),
        StandardCharsets.UTF_8, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING)) {
      for (SqlStatement sqlStatement : sqlStatements) {
        String convertedText = convertAssemblyH2Line(sqlStatement);
        bufferedWriter.write(convertedText);
        bufferedWriter.write('\n');
      }

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String convertAssemblyH2Line(SqlStatement sqlStatement) {
    String convertedText = sqlStatement.getRawText();

    // remove drop table
    if (convertedText.contains("DROP TABLE")) {
      return "";
    }

    Matcher createTableMatcher = CREATE_TABLE_PATTERN.matcher(convertedText);
    Matcher alterTableMatcher = ALTER_TABLE_PATTERN.matcher(convertedText);
    Matcher createIndexOnMatcher = CREATE_INDEX_ON_PATTERN.matcher(convertedText);
    if (createTableMatcher.find()) {
      String createTableName = createTableMatcher.group("tableName");
      // table config
      convertedText = convertTableConfig(convertedText, sqlStatement);
      // index with table
      convertedText = convertIndexWithTable(convertedText, createTableName, sqlStatement);
    } else if (alterTableMatcher.find()) {
      String alterTableName = alterTableMatcher.group("tableName");
      // alter table
      convertedText = convertTableAlter(convertedText, sqlStatement, alterTableMatcher,
          alterTableName);
    } else if (createIndexOnMatcher.find()) {
      String createIndexOnTableName = createIndexOnMatcher.group("tableName");
      // index with table
      convertedText = convertIndexOnTable(convertedText, createIndexOnTableName, sqlStatement);
    }

    // column
    convertedText = convertColumn(convertedText, sqlStatement);
    return convertedText;
  }

  private static String convertTableConfig(String convertedText, SqlStatement sqlStatement) {
    if (convertedText.contains("ENGINE=InnoDB")) {
      convertedText = convertedText.replace("ENGINE=InnoDB", "");
    }
    if (convertedText.contains("DEFAULT CHARSET=utf8mb4")) {
      convertedText = convertedText.replace("DEFAULT CHARSET=utf8mb4", "");
    }
    if (convertedText.contains("ROW_FORMAT=DYNAMIC")) {
      convertedText = convertedText.replace("ROW_FORMAT=DYNAMIC", "");
    }

    return convertedText;
  }

  private static String convertTableAlter(String convertedText, SqlStatement sqlStatement,
      Matcher alterTableMatcher, String tableName) {
    int foundCount = getSubStatementCount(convertedText, sqlStatement, tableName);
    if (foundCount == 0) {
      throw new IllegalStateException("Unsupported alter table statement: " + convertedText);
    } else if (foundCount == 1) {
      // index with alter
      convertedText = convertIndexNameWithAlter(convertedText, tableName, sqlStatement);
      return convertedText;
    } else if (foundCount > 1) {
      convertedText = alterTableMatcher.replaceAll("");
      convertedText = convertTableAlterMulti(convertedText, sqlStatement, tableName);
    }

    return convertedText;
  }

  private static int getSubStatementCount(String convertedText, SqlStatement sqlStatement,
      String tableName) {
    int foundCount = 0;
    Matcher addColumnMatcher = ADD_COLUMN_PATTERN.matcher(convertedText);
    while (addColumnMatcher.find()) {
      foundCount++;
    }
    Matcher modifyColumnMatcher = MODIFY_COLUMN_PATTERN.matcher(convertedText);
    while (modifyColumnMatcher.find()) {
      foundCount++;
    }
    Matcher changeMatcher = CHANGE_PATTERN.matcher(convertedText);
    while (changeMatcher.find()) {
      foundCount++;
    }
    Matcher dropColumnMatcher = DROP_COLUMN_PATTERN.matcher(convertedText);
    while (dropColumnMatcher.find()) {
      foundCount++;
    }
    Matcher addKeyMatcher = ADD_KEY_PATTERN.matcher(convertedText);
    while (addKeyMatcher.find()) {
      foundCount++;
    }
    Matcher addIndexMatcher = ADD_INDEX_PATTERN.matcher(convertedText);
    while (addIndexMatcher.find()) {
      foundCount++;
    }
    Matcher dropIndexMatcher = DROP_INDEX_PATTERN.matcher(convertedText);
    while (dropIndexMatcher.find()) {
      foundCount++;
    }
    return foundCount;
  }

  private static String convertTableAlterMulti(String convertedText, SqlStatement sqlStatement,
      String tableName) {
    Matcher addColumnMatcher = ADD_COLUMN_PATTERN.matcher(convertedText);
    if (addColumnMatcher.find()) {
      convertedText = addColumnMatcher.replaceAll(
          "ALTER TABLE `" + tableName + "` ADD COLUMN `${columnName}`${subStatement};");
    }
    Matcher modifyColumnMatcher = MODIFY_COLUMN_PATTERN.matcher(convertedText);
    if (modifyColumnMatcher.find()) {
      convertedText = modifyColumnMatcher.replaceAll(
          "ALTER TABLE `" + tableName + "` MODIFY COLUMN `${columnName}`${subStatement};");
    }
    Matcher changeMatcher = CHANGE_PATTERN.matcher(convertedText);
    if (changeMatcher.find()) {
      convertedText = changeMatcher.replaceAll("ALTER TABLE `" + tableName
          + "` CHANGE `${oldColumnName` `${newColumnName}` ${subStatement};");
    }

    Matcher dropColumnMatcher = DROP_COLUMN_PATTERN.matcher(convertedText);
    if (dropColumnMatcher.find()) {
      convertedText = dropColumnMatcher.replaceAll(
          "ALTER TABLE `" + tableName + "` DROP `${columnName}`;");
    }
    Matcher addKeyMatcher = ADD_KEY_PATTERN.matcher(convertedText);
    if (addKeyMatcher.find()) {
      convertedText = addKeyMatcher.replaceAll(
          "ALTER TABLE `" + tableName + "` ADD ${indexType} `" + tableName
              + "_${indexName}` ${subStatement};");
      convertedText = removePrefixIndex(convertedText);
    }
    Matcher addIndexMatcher = ADD_INDEX_PATTERN.matcher(convertedText);
    if (addIndexMatcher.find()) {
      convertedText = addIndexMatcher.replaceAll(
          "ALTER TABLE `" + tableName + "` ADD ${indexType} `" + tableName
              + "_${indexName}` ${subStatement};");
      convertedText = removePrefixIndex(convertedText);
    }
    Matcher dropIndexMatcher = DROP_INDEX_PATTERN.matcher(convertedText);
    if (dropIndexMatcher.find()) {
      convertedText = dropIndexMatcher.replaceAll(
          "ALTER TABLE `" + tableName + "` DROP INDEX `" + tableName + "_${indexName}`;");
    }
    return convertedText;
  }

  private static String convertIndexOnTable(String convertedText, String tableName,
      SqlStatement sqlStatement) {
    Matcher createIndexMatcher = CREATE_INDEX_PATTERN.matcher(convertedText);
    if (createIndexMatcher.find()) {
      convertedText = createIndexMatcher.replaceAll(
          "CREATE ${indexType} `" + tableName + "_${indexName}`");
      convertedText = removePrefixIndex(convertedText);
    }
    return convertedText;
  }

  private static String convertIndexWithTable(String convertedText, String tableName,
      SqlStatement sqlStatement) {
    String[] lines = convertedText.split("\n");
    StringJoiner joiner = new StringJoiner("\n");
    for (String line : lines) {
      String convertedLine = line;
      if (convertedLine.contains("KEY")) {
        // replace index name
        // KEY `AppId_ClusterName_GroupName` (`AppId`,`ClusterName`(191),`NamespaceName`(191))
        // ->
        // KEY `tableName_AppId_ClusterName_GroupName` (`AppId`,`ClusterName`(191),`NamespaceName`(191))
        Matcher indexNameMatcher = INDEX_NAME_PATTERN.matcher(convertedLine);
        if (indexNameMatcher.find()) {
          convertedLine = indexNameMatcher.replaceAll("KEY `" + tableName + "_${indexName}`");
        }

        convertedLine = removePrefixIndex(convertedLine);
      }
      joiner.add(convertedLine);
    }
    return joiner.toString();
  }

  private static String convertIndexNameWithAlter(String convertedText, String tableName,
      SqlStatement sqlStatement) {
    Matcher addKeyMatcher = ADD_KEY_PATTERN.matcher(convertedText);
    if (addKeyMatcher.find()) {
      convertedText = addKeyMatcher.replaceAll(
          "ADD ${indexType} `" + tableName + "_${indexName}` ${subStatement};");
      convertedText = removePrefixIndex(convertedText);
    }
    Matcher addIndexMatcher = ADD_INDEX_PATTERN.matcher(convertedText);
    if (addIndexMatcher.find()) {
      convertedText = addIndexMatcher.replaceAll(
          "ADD ${indexType} `" + tableName + "_${indexName}` ${subStatement};");
      convertedText = removePrefixIndex(convertedText);
    }
    Matcher createIndexMatcher = CREATE_INDEX_PATTERN.matcher(convertedText);
    if (createIndexMatcher.find()) {
      convertedText = createIndexMatcher.replaceAll(
          "CREATE ${indexType} `" + tableName + "_${indexName}`");
      convertedText = removePrefixIndex(convertedText);
    }
    Matcher dropIndexMatcher = DROP_INDEX_PATTERN.matcher(convertedText);
    if (dropIndexMatcher.find()) {
      convertedText = dropIndexMatcher.replaceAll("DROP INDEX `" + tableName + "_${indexName}`;");
    }
    return convertedText;
  }

  private static String removePrefixIndex(String convertedText) {
    // convert prefix index
    // CREATE INDEX `IX_NAME` ON App (`AppId`,`ClusterName`(191),`NamespaceName`(191))
    // ->
    // CREATE INDEX `IX_NAME` ON App (`AppId`,`ClusterName`,`NamespaceName`)
    for (Matcher prefixIndexMatcher = PREFIX_INDEX_PATTERN.matcher(convertedText);
        prefixIndexMatcher.find();
        prefixIndexMatcher = PREFIX_INDEX_PATTERN.matcher(convertedText)) {
      convertedText = prefixIndexMatcher.replaceAll("${prefix}`${columnName}`${suffix}");
    }
    return convertedText;
  }

  private static String convertColumn(String convertedText, SqlStatement sqlStatement) {
    // convert bit(1) to boolean
    // `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal'
    // ->
    // `IsDeleted` boolean NOT NULL DEFAULT FALSE
    if (convertedText.contains("bit(1)")) {
      convertedText = convertedText.replace("bit(1)", "boolean");
    }
    if (convertedText.contains("b'0'")) {
      convertedText = convertedText.replace("b'0'", "FALSE");
    }
    if (convertedText.contains("b'1'")) {
      convertedText = convertedText.replace("b'1'", "TRUE");
    }

    return convertedText;
  }
}
