package com.ctrip.framework.apollo.config.data.authentication.properties;

import com.ctrip.framework.apollo.config.data.enums.ApolloClientMessagingType;
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
        ", authentication=" + authentication +
        '}';
  }
}
