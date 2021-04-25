package com.ctrip.framework.apollo.config.data.authentication.properties;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientOauth2AuthenticationProperties {

  /**
   * enable oauth2 authentication
   */
  private Boolean enabled = false;

  /**
   * default client registrationId for oauth2 authentication
   *
   * @see org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction#setDefaultClientRegistrationId(String)
   * @see org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction#setDefaultClientRegistrationId(String)
   */
  private String defaultClientRegistrationId = "";

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getDefaultClientRegistrationId() {
    return defaultClientRegistrationId;
  }

  public void setDefaultClientRegistrationId(String defaultClientRegistrationId) {
    this.defaultClientRegistrationId = defaultClientRegistrationId;
  }

  @Override
  public String toString() {
    return "ApolloClientOauth2AuthenticationProperties{" +
        "enabled=" + enabled +
        ", defaultClientRegistrationId='" + defaultClientRegistrationId + '\'' +
        '}';
  }
}
