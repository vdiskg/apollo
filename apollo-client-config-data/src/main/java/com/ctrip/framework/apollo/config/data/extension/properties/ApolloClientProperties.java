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
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientProperties {

  /**
   * apollo client listening type
   */
  private ApolloClientMessagingType messagingType = ApolloClientMessagingType.LONG_POLLING;

  /**
   * extension configuration
   */
  @NestedConfigurationProperty
  private ApolloClientExtensionProperties extension;

  /**
   * apollo client authentication properties
   */
  @NestedConfigurationProperty
  private ApolloClientAuthenticationProperties authentication;

  public ApolloClientMessagingType getMessagingType() {
    return messagingType;
  }

  public void setMessagingType(
      ApolloClientMessagingType messagingType) {
    this.messagingType = messagingType;
  }

  public ApolloClientExtensionProperties getExtension() {
    return extension;
  }

  public void setExtension(
      ApolloClientExtensionProperties extension) {
    this.extension = extension;
  }

  public ApolloClientAuthenticationProperties getAuthentication() {
    return authentication;
  }

  public void setAuthentication(
      ApolloClientAuthenticationProperties authentication) {
    this.authentication = authentication;
  }

  @Override
  public String toString() {
    return "ApolloClientProperties{" +
        "messagingType=" + messagingType +
        ", extension=" + extension +
        ", authentication=" + authentication +
        '}';
  }
}
