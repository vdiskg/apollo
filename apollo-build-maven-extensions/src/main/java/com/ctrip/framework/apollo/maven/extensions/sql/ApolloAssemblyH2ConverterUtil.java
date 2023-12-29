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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApolloAssemblyH2ConverterUtil {

  public static void convertAssemblyH2(String srcSql, String targetSql,
      Map<String, SqlTemplate> templates) {

    ApolloSqlConverterUtil.ensureDirectories(targetSql);

    try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(srcSql),
        StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(targetSql),
            StandardCharsets.UTF_8, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {
      for (String line = bufferedReader.readLine(); line != null;
          line = bufferedReader.readLine()) {
        String convertedLine = convertAssemblyH2Line(line, templates);
        bufferedWriter.write(convertedLine);
        bufferedWriter.write('\n');
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String convertAssemblyH2Line(String line, Map<String, SqlTemplate> templates) {
    String convertedLine = line;
    ConvertResult result = ApolloSqlConverterUtil.convertTemplate(convertedLine,
        "auto-generated-declaration", templates);
    convertedLine = result.convertedLine();
    if (result.matches()) {
      return convertedLine;
    }
    convertedLine = ApolloSqlConverterUtil.convertTemplate(convertedLine, "h2-function", templates).convertedLine();
    convertedLine = ApolloSqlConverterUtil.convertTemplate(convertedLine, "setup-database", SqlTemplate.empty()).convertedLine();
    convertedLine = ApolloSqlConverterUtil.convertTemplate(convertedLine, "use-database", SqlTemplate.empty()).convertedLine();

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

    // white space
    Pattern whiteSpacePrefixPattern = Pattern.compile("^(\\s+)");
    Matcher whiteSpacePrefixMatcher = whiteSpacePrefixPattern.matcher(convertedLine);
    String whiteSpacePrefix;
    if (whiteSpacePrefixMatcher.find()) {
      whiteSpacePrefix = whiteSpacePrefixMatcher.group(1);
    } else {
      whiteSpacePrefix = "";
    }

    Pattern whiteSpacePattern = Pattern.compile("\\s{2,}");
    Matcher whiteSpaceMatcher = whiteSpacePattern.matcher(convertedLine);
    if (whiteSpaceMatcher.find()) {
      convertedLine = whiteSpaceMatcher.replaceAll(" ");
    }
    return whiteSpacePrefix + convertedLine.trim();
  }
}
