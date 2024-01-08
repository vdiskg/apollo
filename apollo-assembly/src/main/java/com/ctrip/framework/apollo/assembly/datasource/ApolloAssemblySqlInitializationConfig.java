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
package com.ctrip.framework.apollo.assembly.datasource;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties(SqlInitializationProperties.class)
@ConditionalOnProperty(prefix = "spring.sql.init", name = "enabled", matchIfMissing = true)
@Configuration
public class ApolloAssemblySqlInitializationConfig {

  @Bean
  ApolloAssemblyDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
      SqlInitializationProperties properties) {
    return new ApolloAssemblyDataSourceScriptDatabaseInitializer(
        determineDataSource(dataSource, properties.getUsername(), properties.getPassword()), properties);
  }

  private static DataSource determineDataSource(DataSource dataSource, String username, String password) {
    if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
      return DataSourceBuilder.derivedFrom(dataSource)
          .username(username)
          .password(password)
          .type(SimpleDriverDataSource.class)
          .build();
    }
    return dataSource;
  }
}
