/*
 * Copyright 2021 Apollo Authors
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
package com.ctrip.framework.apollo.config.data.extension.properties;

import com.ctrip.framework.apollo.config.data.extension.enums.ApolloClientMessagingType;
import com.ctrip.framework.apollo.config.data.util.ApolloClientWebApplicationTypeUtil;
import org.springframework.boot.WebApplicationType;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientExtensionProperties {

  /**
   * enable apollo client extension(webclient/websocket and authentication)
   */
  private Boolean enabled = false;

  /**
   * apollo client listening type
   */
  private ApolloClientMessagingType messagingType = ApolloClientMessagingType.LONG_POLLING;

  /**
   * the type of the application, it is auto detected from classpath and no necessary to set a value
   * manually
   */
  private WebApplicationType webApplicationType = ApolloClientWebApplicationTypeUtil
      .deduceFromClasspath();

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public ApolloClientMessagingType getMessagingType() {
    return messagingType;
  }

  public void setMessagingType(
      ApolloClientMessagingType messagingType) {
    this.messagingType = messagingType;
  }

  public WebApplicationType getWebApplicationType() {
    return webApplicationType;
  }

  public void setWebApplicationType(WebApplicationType webApplicationType) {
    this.webApplicationType = webApplicationType;
  }

  @Override
  public String toString() {
    return "ApolloClientExtensionProperties{" +
        "enabled=" + enabled +
        ", messagingType=" + messagingType +
        ", webApplicationType=" + webApplicationType +
        '}';
  }
}
