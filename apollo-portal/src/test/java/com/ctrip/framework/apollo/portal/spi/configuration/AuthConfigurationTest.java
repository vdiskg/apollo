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
package com.ctrip.framework.apollo.portal.spi.configuration;

import com.ctrip.framework.apollo.portal.spi.configuration.AuthConfiguration.SpringSecurityAuthAutoConfiguration;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class AuthConfigurationTest {

  public static PasswordEncoder passwordEncoder() {
    return SpringSecurityAuthAutoConfiguration.passwordEncoder();
  }

  public static JdbcUserDetailsManager jdbcUserDetailsManager(
      PasswordEncoder passwordEncoder,
      AuthenticationManagerBuilder auth,
      DataSource datasource,
      EntityManagerFactory entityManagerFactory) throws Exception {
    return SpringSecurityAuthAutoConfiguration
        .jdbcUserDetailsManager(passwordEncoder, auth, datasource, entityManagerFactory);
  }
}