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
