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
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class ApolloSqlConverterUtil {

  public static void ensureDirectories(String targetFilePath) {
    Path path = Paths.get(targetFilePath);
    Path dirPath = path.getParent();
    try {
      Files.createDirectories(dirPath);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String replacePath(String origin, String src, String target) {
    if (!origin.contains(src)) {
      throw new IllegalArgumentException("illegal file path: " + origin);
    }
    return origin.replace(src, target);
  }

  public static Map<String, SqlTemplate> getTemplates(String repositoryDir) {
    String templateDir = repositoryDir + "/scripts/sql-template";
    Map<String, SqlTemplate> templates = new LinkedHashMap<>();
    List<Path> templateFiles = list(Paths.get(templateDir));
    for (Path templatePath : templateFiles) {
      String fileName = templatePath.getFileName().toString();
      String templateName = replacePath(fileName, ".sql", "");

      StringJoiner joiner = new StringJoiner("\n");
      boolean accept = false;
      try (BufferedReader bufferedReader = Files.newBufferedReader(templatePath,
          StandardCharsets.UTF_8)) {
        for (String line = bufferedReader.readLine(); line != null;
            line = bufferedReader.readLine()) {
          if (line.contains("@@template-start@@")) {
            accept = true;
            continue;
          }
          if (line.contains("@@template-end@@")) {
            break;
          }
          if (accept) {
            joiner.add(line);
          }
        }
      } catch (IOException e) {
        throw new UncheckedIOException("failed to open templatePath " + e.getLocalizedMessage(), e);
      }
      String value = joiner.toString();
      SqlTemplate template = new SqlTemplate(templateName, "@@${" + templateName + "}@@", value);
      templates.put(templateName, template);
    }
    return templates;
  }

  public static ConvertResult convertTemplate(String line, String name,
      Map<String, SqlTemplate> templates) {
    return convertTemplate(line, name, templates.get(name));
  }

  public static ConvertResult convertTemplate(String line, String name,
      SqlTemplate template) {
    if (template == null) {
      throw new IllegalArgumentException("template not found: " + name);
    }
    String key = "@@${" + name + "}@@";
    if (line.contains(key)) {
      return new ConvertResult(true, template.getValue());
    }
    return new ConvertResult(false, line);
  }

  public static List<String> getSqlList(String dir, Set<String> ignoreDirs) {
    List<String> sqlList = new ArrayList<>();
    if (Files.exists(Paths.get(dir + "/apolloconfigdb.sql"))) {
      sqlList.add(dir + "/apolloconfigdb.sql");
    }
    if (Files.exists(Paths.get(dir + "/apolloportaldb.sql"))) {
      sqlList.add(dir + "/apolloportaldb.sql");
    }
    List<String> deltaSqlList = getDeltaSqlList(dir, ignoreDirs);
    sqlList.addAll(deltaSqlList);
    return sqlList;
  }

  public static List<String> getSqlList(String dir) {
    return getSqlList(dir, Collections.emptySet());
  }

  public static List<Path> list(Path dir) {
    List<Path> subPathList = new ArrayList<>();
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
      for (Path path : ds) {
        subPathList.add(path);
      }
    } catch (IOException e) {
      throw new UncheckedIOException("failed to open dir " + e.getLocalizedMessage(), e);
    }
    return subPathList;
  }

  private static List<String> getDeltaSqlList(String dir, Set<String> ignoreDirs) {
    Path dirPath = Paths.get(dir + "/delta");
    if (!Files.exists(dirPath)) {
      return Collections.emptyList();
    }
    List<Path> deltaDirList = list(dirPath);
    List<String> deltaSqlList = new ArrayList<>();
    for (Path deltaDir : deltaDirList) {
      if (!Files.isDirectory(deltaDir)) {
        continue;
      }
      if (ignoreDirs.contains(deltaDir.toString().replace("\\", "/"))) {
        continue;
      }
      List<Path> deltaFiles = list(deltaDir);
      for (Path path : deltaFiles) {
        String fileName = path.toString();
        if (fileName.endsWith(".sql")) {
          deltaSqlList.add(fileName.replace("\\", "/"));
        }
      }
    }

    return deltaSqlList;
  }
}
