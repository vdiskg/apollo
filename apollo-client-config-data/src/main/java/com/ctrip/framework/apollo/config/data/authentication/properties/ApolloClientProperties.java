package com.ctrip.framework.apollo.config.data.authentication.properties;

import com.ctrip.framework.apollo.config.data.enums.ApolloClientListeningType;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientProperties {

  /**
   * apollo client listening type
   */
  private ApolloClientListeningType listeningType = ApolloClientListeningType.LONG_POLLING;

  /**
   * apollo client authentication properties
   */
  @NestedConfigurationProperty
  private ApolloClientAuthenticationProperties authentication;

  public ApolloClientListeningType getListeningType() {
    return listeningType;
  }

  public void setListeningType(
      ApolloClientListeningType listeningType) {
    this.listeningType = listeningType;
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
        "listeningType=" + listeningType +
        ", authentication=" + authentication +
        '}';
  }
}
