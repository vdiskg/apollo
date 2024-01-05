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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ApolloMainMysqlConverterUtil {

  public static void convertMainMysql(SqlTemplate sqlTemplate, String targetSql,
      SqlTemplateContext context) {
    String databaseName;
    String srcSql = sqlTemplate.getSrcPath();
    if (srcSql.contains("apolloconfigdb")) {
      databaseName = "ApolloConfigDB";
    } else if (srcSql.contains("apolloportaldb")) {
      databaseName = "ApolloPortalDB";
    } else {
      throw new IllegalArgumentException("unknown database name: " + srcSql);
    }

    ApolloSqlConverterUtil.ensureDirectories(targetSql);

    String rawText = ApolloSqlConverterUtil.process(sqlTemplate, context);

    try (BufferedReader bufferedReader = new BufferedReader(new StringReader(rawText));
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(targetSql),
            StandardCharsets.UTF_8, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {
      for (String line = bufferedReader.readLine(); line != null;
          line = bufferedReader.readLine()) {
        String convertedLine = convertMainMysqlLine(line, databaseName);
        bufferedWriter.write(convertedLine);
        bufferedWriter.write('\n');
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String convertMainMysqlLine(String line, String databaseName) {
    String convertedLine = line;

    convertedLine = convertedLine.replace("P_0_", "");

    convertedLine = convertedLine.replace("C_0_", "");

    convertedLine = convertedLine.replace("ApolloAssemblyDB", databaseName);

    return convertedLine;
  }
}
