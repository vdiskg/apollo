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
package com.ctrip.framework.apollo.config.data.extension.webclient.customizer.spi.impl;

import com.ctrip.framework.apollo.config.data.extension.authentication.oauth2.ApolloClientAuthorizedClientManagerFactory;
import com.ctrip.framework.apollo.config.data.extension.authentication.oauth2.ApolloClientReactiveAuthorizedClientManagerFactory;
import com.ctrip.framework.apollo.config.data.extension.enums.ApolloClientAuthenticationType;
import com.ctrip.framework.apollo.config.data.extension.messaging.ApolloClientPropertiesFactory;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientAuthenticationProperties;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientExtensionProperties;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientHttpBasicAuthenticationProperties;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientOauth2AuthenticationProperties;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.extension.webclient.customizer.ApolloClientHttpBasicAuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.extension.webclient.customizer.ApolloClientOauth2AuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.extension.webclient.customizer.ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.extension.webclient.customizer.spi.ApolloClientWebClientCustomizerFactory;
import com.ctrip.framework.apollo.config.data.extension.webclient.filter.ApolloClientHttpBasicAuthenticationExchangeFilterFunction;
import com.ctrip.framework.apollo.config.data.util.Slf4jLogMessageFormatter;
import com.ctrip.framework.apollo.core.spi.Ordered;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientWebClientAuthenticationCustomizerFactory implements
    ApolloClientWebClientCustomizerFactory {

  private final ApolloClientPropertiesFactory apolloClientPropertiesFactory;

  private final ApolloClientAuthorizedClientManagerFactory apolloClientAuthorizedClientManagerFactory;

  private final ApolloClientReactiveAuthorizedClientManagerFactory apolloClientReactiveAuthorizedClientManagerFactory;

  public ApolloClientWebClientAuthenticationCustomizerFactory() {
    this.apolloClientPropertiesFactory = new ApolloClientPropertiesFactory();
    this.apolloClientAuthorizedClientManagerFactory = new ApolloClientAuthorizedClientManagerFactory();
    this.apolloClientReactiveAuthorizedClientManagerFactory = new ApolloClientReactiveAuthorizedClientManagerFactory();
  }

  @Override
  public WebClientCustomizer createWebClientCustomizer(
      ApolloClientProperties apolloClientProperties, Binder binder, BindHandler bindHandler,
      Log log, ConfigurableBootstrapContext bootstrapContext) {
    ApolloClientExtensionProperties extension = apolloClientProperties.getExtension();
    ApolloClientAuthenticationProperties properties = extension.getAuthentication();
    if (properties == null) {
      log.debug("apollo client authentication properties is empty, authentication disabled");
      return null;
    }
    ApolloClientAuthenticationType authenticationType = properties.getAuthenticationType();
    log.debug(Slf4jLogMessageFormatter
        .format("apollo client authentication type: {}", authenticationType));
    switch (authenticationType) {
      case NONE:
        return null;
      case OAUTH2:
        return this.createOauth2WebClient(properties, binder, bindHandler, log);
      case HTTP_BASIC:
        return this.createHttpBasicWebClient(properties);
      default:
        throw new IllegalStateException("Unexpected value: " + authenticationType);
    }
  }

  private WebClientCustomizer createOauth2WebClient(ApolloClientAuthenticationProperties properties,
      Binder binder, BindHandler bindHandler, Log log) {
    ApolloClientOauth2AuthenticationProperties apolloClientOauth2AuthenticationProperties = properties
        .getOauth2();
    if (apolloClientOauth2AuthenticationProperties == null) {
      throw new IllegalArgumentException(
          "apolloClientOauth2AuthenticationProperties must not be null");
    }
    OAuth2ClientProperties oauth2ClientProperties = this.apolloClientPropertiesFactory
        .createOauth2ClientProperties(binder, bindHandler);
    if (oauth2ClientProperties == null) {
      throw new IllegalArgumentException("oauth2ClientProperties must not be null");
    }
    WebApplicationType webApplicationType = apolloClientOauth2AuthenticationProperties
        .getWebApplicationType();
    if (WebApplicationType.REACTIVE.equals(webApplicationType)) {
      log.debug("apollo client reactive oauth2 client enabled");
      return this
          .getReactiveOauth2WebClient(oauth2ClientProperties, properties);
    }
    log.debug("apollo client imperative oauth2 client enabled");
    return this.getOauth2WebClient(oauth2ClientProperties, properties);
  }

  /**
   * reactive oauth2 authentication webclient
   */
  private WebClientCustomizer getReactiveOauth2WebClient(
      OAuth2ClientProperties oauth2ClientProperties,
      ApolloClientAuthenticationProperties properties) {
    ReactiveOAuth2AuthorizedClientManager authorizedClientManager = this.apolloClientReactiveAuthorizedClientManagerFactory
        .createAuthorizedClientManager(oauth2ClientProperties);
    return this
        .reactiveOauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
  }

  private ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer reactiveOauth2AuthenticationWebClientCustomizer(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction =
        new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    filterFunction.setDefaultOAuth2AuthorizedClient(true);
    filterFunction
        .setDefaultClientRegistrationId(properties.getOauth2().getDefaultClientRegistrationId());
    return new ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer(filterFunction);
  }

  /**
   * oauth2 authentication webclient
   */
  private WebClientCustomizer getOauth2WebClient(OAuth2ClientProperties oauth2ClientProperties,
      ApolloClientAuthenticationProperties properties) {
    OAuth2AuthorizedClientManager authorizedClientManager = this.apolloClientAuthorizedClientManagerFactory
        .createAuthorizedClientManager(oauth2ClientProperties);
    return this.oauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
  }

  private ApolloClientOauth2AuthenticationWebClientCustomizer oauth2AuthenticationWebClientCustomizer(
      OAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    ServletOAuth2AuthorizedClientExchangeFilterFunction filterFunction =
        new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    filterFunction.setDefaultOAuth2AuthorizedClient(true);
    filterFunction
        .setDefaultClientRegistrationId(properties.getOauth2().getDefaultClientRegistrationId());
    return new ApolloClientOauth2AuthenticationWebClientCustomizer(filterFunction);
  }

  /**
   * http basic authentication webclient
   */
  private WebClientCustomizer createHttpBasicWebClient(
      ApolloClientAuthenticationProperties properties) {
    properties.getHttpBasic().validate();
    return this.httpBasicAuthenticationWebClientCustomizer(properties);
  }

  private ApolloClientHttpBasicAuthenticationWebClientCustomizer httpBasicAuthenticationWebClientCustomizer(
      ApolloClientAuthenticationProperties properties) {
    ApolloClientHttpBasicAuthenticationProperties httpBasic = properties.getHttpBasic();
    if (httpBasic.validateUsernameAndPassword()) {
      return new ApolloClientHttpBasicAuthenticationWebClientCustomizer(
          new ApolloClientHttpBasicAuthenticationExchangeFilterFunction(httpBasic.getUsername(),
              httpBasic.getPassword()));
    }
    if (httpBasic.validateEncodedCredentials()) {
      return new ApolloClientHttpBasicAuthenticationWebClientCustomizer(
          new ApolloClientHttpBasicAuthenticationExchangeFilterFunction(
              httpBasic.getEncodedCredentials()));
    }
    throw new IllegalStateException("username password pair or encodedCredentials expected");
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE;
  }
}
