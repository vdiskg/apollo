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

import com.ctrip.framework.apollo.config.data.extension.enums.ApolloClientAuthenticationType;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientAuthenticationProperties {

  /**
   * authentication type
   */
  private ApolloClientAuthenticationType authenticationType = ApolloClientAuthenticationType.NONE;

  /**
   * http-basic authentication
   */
  @NestedConfigurationProperty
  private ApolloClientHttpBasicAuthenticationProperties httpBasic;

  /**
   * oauth2 authentication
   */
  @NestedConfigurationProperty
  private ApolloClientOauth2AuthenticationProperties oauth2;

  public ApolloClientAuthenticationType getAuthenticationType() {
    return authenticationType;
  }

  public void setAuthenticationType(
      ApolloClientAuthenticationType authenticationType) {
    this.authenticationType = authenticationType;
  }

  public ApolloClientHttpBasicAuthenticationProperties getHttpBasic() {
    return httpBasic;
  }

  public void setHttpBasic(
      ApolloClientHttpBasicAuthenticationProperties httpBasic) {
    this.httpBasic = httpBasic;
  }

  public ApolloClientOauth2AuthenticationProperties getOauth2() {
    return oauth2;
  }

  public void setOauth2(
      ApolloClientOauth2AuthenticationProperties oauth2) {
    this.oauth2 = oauth2;
  }

  @Override
  public String toString() {
    return "ApolloClientAuthenticationProperties{" +
        "authenticationType=" + authenticationType +
        ", httpBasic=" + httpBasic +
        ", oauth2=" + oauth2 +
        '}';
  }
}
