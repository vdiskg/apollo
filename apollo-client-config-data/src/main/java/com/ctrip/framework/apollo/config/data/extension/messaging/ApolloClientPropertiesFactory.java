package com.ctrip.framework.apollo.config.data.extension.messaging;

import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientPropertiesFactory {

  public static final String PROPERTIES_PREFIX = "apollo.client";

  public ApolloClientProperties createApolloClientProperties(
      Binder binder,
      BindHandler bindHandler) {
    return binder.bind(PROPERTIES_PREFIX,
        Bindable.of(ApolloClientProperties.class), bindHandler).orElse(null);
  }

  public OAuth2ClientProperties createOauth2ClientProperties(Binder binder,
      BindHandler bindHandler) {
    return binder.bind("spring.security.oauth2.client", Bindable.of(OAuth2ClientProperties.class),
        bindHandler).orElse(null);
  }
}
