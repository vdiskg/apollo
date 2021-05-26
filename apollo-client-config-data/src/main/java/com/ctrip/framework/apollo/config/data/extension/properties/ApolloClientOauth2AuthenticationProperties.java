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

import com.ctrip.framework.apollo.config.data.util.ApolloClientWebApplicationTypeUtil;
import org.springframework.boot.WebApplicationType;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientOauth2AuthenticationProperties {

  /**
   * default client registrationId for oauth2 authentication
   *
   * @see org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction#setDefaultClientRegistrationId(String)
   * @see org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction#setDefaultClientRegistrationId(String)
   */
  private String defaultClientRegistrationId = "";

  /**
   * the type of oauth2 client to be created, auto detected from classpath
   */
  private WebApplicationType webApplicationType = ApolloClientWebApplicationTypeUtil
      .deduceFromClasspath();

  public String getDefaultClientRegistrationId() {
    return defaultClientRegistrationId;
  }

  public void setDefaultClientRegistrationId(String defaultClientRegistrationId) {
    this.defaultClientRegistrationId = defaultClientRegistrationId;
  }

  public WebApplicationType getWebApplicationType() {
    return webApplicationType;
  }

  public void setWebApplicationType(WebApplicationType webApplicationType) {
    this.webApplicationType = webApplicationType;
  }

  @Override
  public String toString() {
    return "ApolloClientOauth2AuthenticationProperties{" +
        "defaultClientRegistrationId='" + defaultClientRegistrationId + '\'' +
        ", webApplicationType=" + webApplicationType +
        '}';
  }
}
