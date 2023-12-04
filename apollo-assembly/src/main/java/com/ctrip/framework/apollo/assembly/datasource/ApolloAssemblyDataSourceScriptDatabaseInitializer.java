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
package com.ctrip.framework.apollo.assembly.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.init.PlatformPlaceholderDatabaseDriverResolver;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.util.StringUtils;

public class ApolloAssemblyDataSourceScriptDatabaseInitializer extends
    SqlDataSourceScriptDatabaseInitializer {

  public ApolloAssemblyDataSourceScriptDatabaseInitializer(DataSource dataSource,
      SqlInitializationProperties properties) {
    super(dataSource, getSettings(dataSource, properties));
  }

  public static DatabaseInitializationSettings getSettings(DataSource dataSource,
      SqlInitializationProperties properties) {

    PlatformPlaceholderDatabaseDriverResolver platformResolver = new PlatformPlaceholderDatabaseDriverResolver().withDriverPlatform(
        DatabaseDriver.MARIADB, "mysql");

    List<String> schemaLocations = resolveLocations(properties.getSchemaLocations(),
        platformResolver,
        dataSource, properties);
    List<String> dataLocations = resolveLocations(properties.getDataLocations(), platformResolver,
        dataSource, properties);

    DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
    settings.setSchemaLocations(
        scriptLocations(schemaLocations, "schema", properties.getPlatform()));
    settings.setDataLocations(scriptLocations(dataLocations, "data", properties.getPlatform()));
    settings.setContinueOnError(properties.isContinueOnError());
    settings.setSeparator(properties.getSeparator());
    settings.setEncoding(properties.getEncoding());
    settings.setMode(properties.getMode());
    return settings;
  }


  private static List<String> resolveLocations(Collection<String> locations,
      PlatformPlaceholderDatabaseDriverResolver platformResolver, DataSource dataSource,
      SqlInitializationProperties properties) {

    if (StringUtils.hasText(properties.getPlatform())) {
      return platformResolver.resolveAll(properties.getPlatform(),
          locations.toArray(new String[0]));
    }
    return platformResolver.resolveAll(dataSource, locations.toArray(new String[0]));
  }

  private static List<String> scriptLocations(List<String> locations, String fallback,
      String platform) {
    if (locations != null) {
      return locations;
    }
    List<String> fallbackLocations = new ArrayList<>();
    fallbackLocations.add("optional:classpath*:" + fallback + "-" + platform + ".sql");
    fallbackLocations.add("optional:classpath*:" + fallback + ".sql");
    return fallbackLocations;
  }
}
