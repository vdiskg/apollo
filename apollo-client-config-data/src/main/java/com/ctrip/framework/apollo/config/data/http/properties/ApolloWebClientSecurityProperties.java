package com.ctrip.framework.apollo.config.data.http.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloWebClientSecurityProperties implements InitializingBean {

  /**
   * http-basic authentication
   */
  @NestedConfigurationProperty
  private ApolloWebClientHttpBasicAuthenticationProperties httpBasic;

  /**
   * oauth2 authentication
   */
  @NestedConfigurationProperty
  private ApolloWebClientOauth2AuthenticationProperties oauth2;

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

  public ApolloWebClientHttpBasicAuthenticationProperties getHttpBasic() {
    return httpBasic;
  }

  public void setHttpBasic(
      ApolloWebClientHttpBasicAuthenticationProperties httpBasic) {
    this.httpBasic = httpBasic;
  }

  public ApolloWebClientOauth2AuthenticationProperties getOauth2() {
    return oauth2;
  }

  public void setOauth2(
      ApolloWebClientOauth2AuthenticationProperties oauth2) {
    this.oauth2 = oauth2;
  }

  @Override
  public String toString() {
    return "ApolloWebClientSecurityProperties{" +
        "httpBasic=" + httpBasic +
        ", oauth2=" + oauth2 +
        '}';
  }
}
