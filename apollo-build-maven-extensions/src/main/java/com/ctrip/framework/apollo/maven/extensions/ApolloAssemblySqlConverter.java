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
package com.ctrip.framework.apollo.maven.extensions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApolloAssemblySqlConverter {

  public static void main(String[] args) {
    String moduleDir = args[0];
    String unixModuleDir = moduleDir.replace("\\", "/");
    String repositoryDir = unixModuleDir.replace("/apollo-build-maven-extensions", "");

    String assemblyMysqlDir = repositoryDir + "/scripts/sql/assembly/mysql";

    List<String> assemblyMysqlSqlList = new ArrayList<>();
    assemblyMysqlSqlList.add(assemblyMysqlDir + "/apolloconfigdb.sql");
    assemblyMysqlSqlList.add(assemblyMysqlDir + "/apolloportaldb.sql");
    List<String> assemblyMysqlDeltaSqlList = getAssemblyMysqlDeltaSqlList(assemblyMysqlDir);
    assemblyMysqlSqlList.addAll(assemblyMysqlDeltaSqlList);

    // '/scripts/sql/assembly/mysql' -> '/scripts/sql'
    convertMainMysqlList(assemblyMysqlSqlList, assemblyMysqlDir, repositoryDir);

    // '/scripts/sql/assembly/mysql' -> '/scripts/sql/assembly/h2'
    convertAssemblyH2List(assemblyMysqlSqlList, assemblyMysqlDir, repositoryDir);
  }

  private static void convertMainMysqlList(List<String> assemblyMysqlSqlList,
      String assemblyMysqlDir, String repositoryDir) {
    String targetDir = repositoryDir + "/scripts/sql";
    for (String filePath : assemblyMysqlSqlList) {
      if (!filePath.contains(assemblyMysqlDir)) {
        throw new IllegalArgumentException("illegal file path: " + filePath);
      }
      String targetFilePath = filePath.replace(assemblyMysqlDir, targetDir);
      String databaseName;
      if (filePath.contains("apolloconfigdb")) {
        databaseName = "ApolloConfigDB";
      } else if (filePath.contains("apolloportaldb")) {
        databaseName = "ApolloPortalDB";
      } else {
        throw new IllegalArgumentException("unknown database name: " + filePath);
      }

      ensureDirectories(targetFilePath);

      try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath),
          StandardCharsets.UTF_8);
          BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(targetFilePath),
              StandardCharsets.UTF_8, StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING)) {
        for (String line = bufferedReader.readLine(); line != null;
            line = bufferedReader.readLine()) {
          String convertedLine = line.replace("P_0_", "").replace("C_0_", "")
              .replace("ApolloAssemblyDB", databaseName);
          bufferedWriter.write(convertedLine);
          bufferedWriter.write('\n');
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static void ensureDirectories(String targetFilePath) {
    Path path = Paths.get(targetFilePath);
    Path dirPath = path.getParent();
    try {
      Files.createDirectories(dirPath);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void convertAssemblyH2List(List<String> assemblyMysqlSqlList,
      String assemblyMysqlDir, String repositoryDir) {
    String targetDir = repositoryDir + "/scripts/sql/assembly/h2";
    for (String filePath : assemblyMysqlSqlList) {
      if (!filePath.contains(assemblyMysqlDir)) {
        throw new IllegalArgumentException("illegal file path: " + filePath);
      }
      String targetFilePath = filePath.replace(assemblyMysqlDir, targetDir);

      ensureDirectories(targetFilePath);

      try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath),
          StandardCharsets.UTF_8);
          BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(targetFilePath),
              StandardCharsets.UTF_8, StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING)) {
        for (String line = bufferedReader.readLine(); line != null;
            line = bufferedReader.readLine()) {
          String convertedLine = convertAssemblyH2ListLine(line);
          bufferedWriter.write(convertedLine);
          bufferedWriter.write('\n');
        }
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  private static String convertAssemblyH2ListLine(String line) {
    // database config
    if (line.contains("Use ")
        || line.contains("DROP TABLE")) {
      return "";
    } else if (line.contains("CREATE DATABASE")) {
      return "CREATE ALIAS IF NOT EXISTS UNIX_TIMESTAMP FOR \"com.ctrip.framework.apollo.common.jpa.H2Function.unixTimestamp\";";
    }
    String convertedLine = line;

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
    if (line.contains("KEY")) {
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
    if (line.contains("bit(1)")) {
      convertedLine = convertedLine.replace("bit(1)", "boolean");
    }
    if (line.contains("b'0'")) {
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

  private static List<String> getAssemblyMysqlDeltaSqlList(String assemblyMysqlDir) {
    Path dir = Paths.get(assemblyMysqlDir + "/delta");
    if (!Files.exists(dir)) {
      return Collections.emptyList();
    }
    List<Path> deltaDirList = new ArrayList<>();
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
      for (Path path : ds) {
        if (Files.isDirectory(path)) {
          deltaDirList.add(path);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("failed to open assemblyMysqlDir" + e.getLocalizedMessage(),
          e);
    }
    List<String> assemblyMysqlDeltaSqlList = new ArrayList<>();
    for (Path deltaDir : deltaDirList) {
      try (DirectoryStream<Path> ds = Files.newDirectoryStream(deltaDir)) {
        for (Path path : ds) {
          String fileName = path.toString();
          if (fileName.endsWith(".sql")) {
            assemblyMysqlDeltaSqlList.add(fileName.replace("\\", "/"));
          }
        }
      } catch (IOException e) {
        throw new UncheckedIOException("failed to open assemblyMysqlDir" + e.getLocalizedMessage(),
            e);
      }
    }

    return assemblyMysqlDeltaSqlList;
  }
}
