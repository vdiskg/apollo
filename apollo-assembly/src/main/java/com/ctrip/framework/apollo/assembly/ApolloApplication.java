/*
 * Copyright 2022 Apollo Authors
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
package com.ctrip.framework.apollo.assembly;

import com.ctrip.framework.apollo.adminservice.AdminServiceApplication;
import com.ctrip.framework.apollo.configservice.ConfigServiceApplication;
import com.ctrip.framework.apollo.portal.PortalApplication;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class})
public class ApolloApplication {

  private static final Logger logger = LoggerFactory.getLogger(ApolloApplication.class);

  public static void main(String[] args) throws Exception {
    /**
     * Common
     */
    ConfigurableApplicationContext commonContext =
        new SpringApplicationBuilder(ApolloApplication.class).web(WebApplicationType.NONE).run(args);
    logger.info(commonContext.getId() + " commonContext isActive: " + commonContext.isActive());

    /**
     * ConfigService
     */
    if (commonContext.getEnvironment().containsProperty("configservice")) {
      ConfigurableApplicationContext configContext = createSpringApplication("config",
          commonContext, ConfigServiceApplication.class).run(args);
      logger.info(configContext.getId() + " configContext isActive: " + configContext.isActive()
          + "\n\n\n\n ========================================");
    }

    /**
     * AdminService
     */
    if (commonContext.getEnvironment().containsProperty("adminservice")) {
      ConfigurableApplicationContext adminContext = createSpringApplication("config", commonContext,
          AdminServiceApplication.class).run(args);
      logger.info(adminContext.getId() + " adminContext isActive: " + adminContext.isActive()
          + "\n\n\n\n ========================================");
    }

    /**
     * Portal
     */
    if (commonContext.getEnvironment().containsProperty("portal")) {
      ConfigurableApplicationContext portalContext = createSpringApplication("portal",
          commonContext,
          PortalApplication.class).run(args);
      String[] activeProfiles = portalContext.getEnvironment().getActiveProfiles();
      logger.info(portalContext.getId() + " portalContext isActive: " + portalContext.isActive()
          + "\n\n\n\n ========================================");
    }
  }

  private static SpringApplicationBuilder createSpringApplication(String environmentPrefix,
      ConfigurableApplicationContext commonContext, Class<?>... primarySources) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(primarySources).parent(
            commonContext)
        .sources(RefreshScope.class).environmentPrefix(environmentPrefix);

    @SuppressWarnings("unchecked")
    List<String> profiles = (List<String>) commonContext.getEnvironment()
        .getProperty(environmentPrefix + ".spring.profiles.active", List.class);
    if (profiles != null && !profiles.isEmpty()) {
      builder.profiles(StringUtils.toStringArray(profiles));
    }
    return builder;
  }

}
