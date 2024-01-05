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
package com.ctrip.framework.apollo.build.sql.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApolloAssemblyH2ConverterUtil {

  private static final Pattern TABLE_COMMENT_PATTERN = Pattern.compile("COMMENT='[^']*'");
  private static final Pattern INDEX_NAME_PATTERN = Pattern.compile("KEY *`[a-zA-Z0-9\\-_]+` *");
  private static final Pattern PREFIX_INDEX_PATTERN = Pattern.compile(
      "(`[a-zA-Z0-9\\-_]+`)\\([0-9]+\\)");
  private static final Pattern COLUMN_COMMENT_PATTERN = Pattern.compile("COMMENT *'[^']*'");

  public static void convertAssemblyH2(SqlTemplate sqlTemplate, String targetSql,
      SqlTemplateContext context) {

    ApolloSqlConverterUtil.ensureDirectories(targetSql);

    String rawText = ApolloSqlConverterUtil.process(sqlTemplate, context);

    try (BufferedReader bufferedReader = new BufferedReader(new StringReader(rawText));
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(targetSql),
            StandardCharsets.UTF_8, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {
      for (String line = bufferedReader.readLine(); line != null;
          line = bufferedReader.readLine()) {
        String convertedLine = convertAssemblyH2Line(line);
        bufferedWriter.write(convertedLine);
        bufferedWriter.write('\n');
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String convertAssemblyH2Line(String line) {
    String convertedLine = line;

    // remove drop table
    if (convertedLine.contains("DROP TABLE")) {
      return "";
    }

    // table config
    convertedLine = convertTableConfig(convertedLine);

    // index
    convertedLine = convertIndex(convertedLine);

    // column
    convertedLine = convertColumn(convertedLine);
    return convertedLine;
  }

  private static String convertTableConfig(String convertedLine) {
    if (convertedLine.contains("ENGINE=InnoDB")) {
      convertedLine = convertedLine.replace("ENGINE=InnoDB", "");
    }
    if (convertedLine.contains("DEFAULT CHARSET=utf8mb4")) {
      convertedLine = convertedLine.replace("DEFAULT CHARSET=utf8mb4", "");
    }
    if (convertedLine.contains("ROW_FORMAT=DYNAMIC")) {
      convertedLine = convertedLine.replace("ROW_FORMAT=DYNAMIC", "");
    }

    // remove table comment
    Matcher tableCommentMatcher = TABLE_COMMENT_PATTERN.matcher(convertedLine);
    if (tableCommentMatcher.find()) {
      convertedLine = tableCommentMatcher.replaceAll("");
    }

    return convertedLine;
  }

  private static String convertIndex(String convertedLine) {
    if (convertedLine.contains("KEY")) {
      // remove index name
      // KEY `AppId_ClusterName_GroupName` (`AppId`,`ClusterName`(191),`NamespaceName`(191))
      // ->
      // KEY (`AppId`,`ClusterName`(191),`NamespaceName`(191))
      Matcher indexNameMatcher = INDEX_NAME_PATTERN.matcher(convertedLine);
      if (indexNameMatcher.find()) {
        convertedLine = indexNameMatcher.replaceAll("KEY ");
      }

      // convert prefix index
      // KEY (`AppId`,`ClusterName`(191),`NamespaceName`(191))
      // ->
      // KEY (`AppId`,`ClusterName`,`NamespaceName`)
      for (Matcher prefixIndexMatcher = PREFIX_INDEX_PATTERN.matcher(convertedLine);
          prefixIndexMatcher.find();
          prefixIndexMatcher = PREFIX_INDEX_PATTERN.matcher(convertedLine)) {
        convertedLine = prefixIndexMatcher.replaceAll("$1");
      }
    }
    return convertedLine;
  }

  private static String convertColumn(String convertedLine) {
    // convert bit(1) to boolean
    // `IsDeleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1: deleted, 0: normal'
    // ->
    // `IsDeleted` boolean NOT NULL DEFAULT FALSE
    if (convertedLine.contains("bit(1)")) {
      convertedLine = convertedLine.replace("bit(1)", "boolean");
    }
    if (convertedLine.contains("b'0'")) {
      convertedLine = convertedLine.replace("b'0'", "FALSE");
    }

    // remove column comment
    Matcher columnCommentMatcher = COLUMN_COMMENT_PATTERN.matcher(convertedLine);
    if (columnCommentMatcher.find()) {
      convertedLine = columnCommentMatcher.replaceAll("");
    }

    return convertedLine;
  }
}
