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
package com.ctrip.framework.apollo.common.jpa;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jpa.table-prefix")
public class TablePrefixProperties {

  /**
   * The table name prefix of config module
   */
  private String configPrefix = "";

  /**
   * The table name prefix of portal module
   */
  private String portalPrefix = "";

  public String getConfigPrefix() {
    return this.configPrefix;
  }

  public void setConfigPrefix(String configPrefix) {
    this.configPrefix = configPrefix;
  }

  public String getPortalPrefix() {
    return this.portalPrefix;
  }

  public void setPortalPrefix(String portalPrefix) {
    this.portalPrefix = portalPrefix;
  }
}
