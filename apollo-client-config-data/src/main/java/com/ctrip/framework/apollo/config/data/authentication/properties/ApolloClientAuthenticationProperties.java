package com.ctrip.framework.apollo.config.data.authentication.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientAuthenticationProperties implements InitializingBean {

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

  @Override
  public void afterPropertiesSet() {
    this.validate();
  }

  public void validate() {
    if (this.getOauth2() != null && this.getOauth2().getEnabled()
        && this.getHttpBasic() != null && this.getHttpBasic().getEnabled()) {
      throw new IllegalStateException("duplicated authentication enabled");
    }
  }

  public boolean authenticationEnabled() {
    if (this.getOauth2() != null && this.getOauth2().getEnabled()) {
      return true;
    }
    return this.getHttpBasic() != null && this.getHttpBasic().getEnabled();
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
        "httpBasic=" + httpBasic +
        ", oauth2=" + oauth2 +
        '}';
  }
}
