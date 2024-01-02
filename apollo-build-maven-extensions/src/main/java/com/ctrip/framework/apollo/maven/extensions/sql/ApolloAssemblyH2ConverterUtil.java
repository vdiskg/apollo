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
    // database config
    if (convertedLine.contains("DROP TABLE")) {
      return "";
    }

    // table config
    if (convertedLine.contains("ENGINE=InnoDB")) {
      convertedLine = convertedLine.replace("ENGINE=InnoDB", "");
    }
    if (convertedLine.contains("DEFAULT CHARSET=utf8mb4")) {
      convertedLine = convertedLine.replace("DEFAULT CHARSET=utf8mb4", "");
    }
    if (convertedLine.contains("ROW_FORMAT=DYNAMIC")) {
      convertedLine = convertedLine.replace("ROW_FORMAT=DYNAMIC", "");
    }
    Pattern tableCommentPattern = Pattern.compile("COMMENT='[^']*'");
    Matcher tableCommentMatcher = tableCommentPattern.matcher(convertedLine);
    if (tableCommentMatcher.find()) {
      convertedLine = tableCommentMatcher.replaceAll("");
    }

    // index
    if (convertedLine.contains("KEY")) {
      Pattern indexNamePattern = Pattern.compile("KEY *`[a-zA-Z0-9\\-_]+` *");
      Matcher indexNameMatcher = indexNamePattern.matcher(convertedLine);
      if (indexNameMatcher.find()) {
        convertedLine = indexNameMatcher.replaceAll("KEY ");
      }
      Pattern indexPrefixPattern = Pattern.compile("(`[a-zA-Z0-9\\-_]+`)\\([0-9]+\\)");
      for (Matcher indexPrefixMatcher = indexPrefixPattern.matcher(convertedLine);
          indexPrefixMatcher.find();
          indexPrefixMatcher = indexPrefixPattern.matcher(convertedLine)) {
        convertedLine = indexPrefixMatcher.replaceAll("$1");
      }
    }

    // column config
    if (convertedLine.contains("bit(1)")) {
      convertedLine = convertedLine.replace("bit(1)", "boolean");
    }
    if (convertedLine.contains("b'0'")) {
      convertedLine = convertedLine.replace("b'0'", "FALSE");
    }
    Pattern columnCommentPattern = Pattern.compile("COMMENT *'[^']*'");
    Matcher columnCommentMatcher = columnCommentPattern.matcher(convertedLine);
    if (columnCommentMatcher.find()) {
      convertedLine = columnCommentMatcher.replaceAll("");
    }
    return convertedLine;
  }
}
