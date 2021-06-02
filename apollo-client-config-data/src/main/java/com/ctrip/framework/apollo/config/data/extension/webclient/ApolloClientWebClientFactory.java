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
package com.ctrip.framework.apollo.config.data.extension.webclient;

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
import com.ctrip.framework.apollo.config.data.extension.webclient.filter.ApolloClientHttpBasicAuthenticationExchangeFilterFunction;
import com.ctrip.framework.apollo.config.data.util.Slf4jLogMessageFormatter;
import org.apache.commons.logging.Log;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientWebClientFactory {

  private final Log log;

  private final ApolloClientPropertiesFactory apolloClientPropertiesFactory;

  private final ApolloClientAuthorizedClientManagerFactory apolloClientAuthorizedClientManagerFactory;

  private final ApolloClientReactiveAuthorizedClientManagerFactory apolloClientReactiveAuthorizedClientManagerFactory;

  public ApolloClientWebClientFactory(Log log) {
    this.log = log;
    this.apolloClientPropertiesFactory = new ApolloClientPropertiesFactory();
    this.apolloClientAuthorizedClientManagerFactory = new ApolloClientAuthorizedClientManagerFactory();
    this.apolloClientReactiveAuthorizedClientManagerFactory = new ApolloClientReactiveAuthorizedClientManagerFactory();
  }

  public WebClient.Builder createWebClient(ApolloClientProperties apolloClientProperties,
      Binder binder,
      BindHandler bindHandler) {
    ApolloClientExtensionProperties extension = apolloClientProperties.getExtension();
    ApolloClientAuthenticationProperties properties = extension.getAuthentication();
    if (properties == null) {
      log.debug("apollo client authentication properties is empty, authentication disabled");
      return WebClient.builder();
    }
    ApolloClientAuthenticationType authenticationType = properties.getAuthenticationType();
    log.debug(Slf4jLogMessageFormatter
        .format("apollo client authentication type: {}", authenticationType));
    switch (authenticationType) {
      case NONE:
        return WebClient.builder();
      case OAUTH2:
        return this.createOauth2WebClient(properties, binder, bindHandler);
      case HTTP_BASIC:
        return this.createHttpBasicWebClient(properties, binder, bindHandler);
      default:
        throw new IllegalStateException("Unexpected value: " + authenticationType);
    }
  }

  private WebClient.Builder createOauth2WebClient(ApolloClientAuthenticationProperties properties,
      Binder binder,
      BindHandler bindHandler) {
    ApolloClientOauth2AuthenticationProperties oauth2AuthenticationProperties = properties
        .getOauth2();
    if (oauth2AuthenticationProperties == null) {
      throw new IllegalArgumentException("oauth2AuthenticationProperties must not be null");
    }
    OAuth2ClientProperties oauth2ClientProperties = this.apolloClientPropertiesFactory
        .createOauth2ClientProperties(binder, bindHandler);
    if (oauth2ClientProperties == null) {
      throw new IllegalArgumentException("oauth2ClientProperties must not be null");
    }
    WebApplicationType webApplicationType = oauth2AuthenticationProperties.getWebApplicationType();
    if (WebApplicationType.REACTIVE.equals(webApplicationType)) {
      log.debug("apollo client reactive oauth2 client enabled");
      return this
          .getReactiveOauth2WebClient(oauth2ClientProperties, properties, binder, bindHandler);
    }
    log.debug("apollo client imperative oauth2 client enabled");
    return this.getOauth2WebClient(oauth2ClientProperties, properties, binder, bindHandler);
  }

  /**
   * reactive oauth2 authentication webclient
   */
  private WebClient.Builder getReactiveOauth2WebClient(
      OAuth2ClientProperties oauth2ClientProperties,
      ApolloClientAuthenticationProperties properties, Binder binder,
      BindHandler bindHandler) {
    ReactiveOAuth2AuthorizedClientManager authorizedClientManager = this.apolloClientReactiveAuthorizedClientManagerFactory
        .createAuthorizedClientManager(oauth2ClientProperties);
    return this.reactiveOauth2WebClient(authorizedClientManager, properties);
  }

  private WebClient.Builder reactiveOauth2WebClient(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer customizer = this
        .reactiveOauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder;
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
  private WebClient.Builder getOauth2WebClient(OAuth2ClientProperties oauth2ClientProperties,
      ApolloClientAuthenticationProperties properties, Binder binder,
      BindHandler bindHandler) {
    OAuth2AuthorizedClientManager authorizedClientManager = this.apolloClientAuthorizedClientManagerFactory
        .createAuthorizedClientManager(oauth2ClientProperties);
    return this.oauth2WebClient(authorizedClientManager, properties);
  }

  private WebClient.Builder oauth2WebClient(OAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    ApolloClientOauth2AuthenticationWebClientCustomizer customizer = this
        .oauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder;
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
  private WebClient.Builder createHttpBasicWebClient(
      ApolloClientAuthenticationProperties properties,
      Binder binder,
      BindHandler bindHandler) {
    properties.getHttpBasic().validate();
    return this.httpBasicWebClient(properties);
  }

  private WebClient.Builder httpBasicWebClient(ApolloClientAuthenticationProperties properties) {
    ApolloClientHttpBasicAuthenticationWebClientCustomizer customizer = this
        .httpBasicAuthenticationWebClientCustomizer(properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder;
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
}
