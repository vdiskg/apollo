package com.ctrip.framework.apollo.config.data.authentication;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientAuthenticationProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientAuthenticationPropertiesFactory {

  public static final String AUTHENTICATION_PROPERTIES_PREFIX = "apollo.client.authentication";

  public ApolloClientAuthenticationProperties createApolloClientAuthenticationProperties(
      Binder binder,
      BindHandler bindHandler) {
    return binder.bind(AUTHENTICATION_PROPERTIES_PREFIX,
        Bindable.of(ApolloClientAuthenticationProperties.class), bindHandler).orElse(null);
  }

  public OAuth2ClientProperties createOauth2ClientProperties(Binder binder,
      BindHandler bindHandler) {
    return binder.bind("spring.security.oauth2.client", Bindable.of(OAuth2ClientProperties.class),
        bindHandler).orElse(null);
  }
}
