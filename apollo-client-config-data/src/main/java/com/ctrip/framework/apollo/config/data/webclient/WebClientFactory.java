package com.ctrip.framework.apollo.config.data.webclient;

import com.ctrip.framework.apollo.config.data.authentication.ApolloClientAuthenticationPropertiesFactory;
import com.ctrip.framework.apollo.config.data.authentication.oauth2.ApolloClientAuthorizedClientManagerFactory;
import com.ctrip.framework.apollo.config.data.authentication.oauth2.ApolloClientReactiveAuthorizedClientManagerFactory;
import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientAuthenticationProperties;
import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientHttpBasicAuthenticationProperties;
import com.ctrip.framework.apollo.config.data.util.WebApplicationTypeUtil;
import com.ctrip.framework.apollo.config.data.webclient.customizer.HttpBasicAuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.webclient.customizer.Oauth2AuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.webclient.customizer.Oauth2ReactiveAuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.webclient.filter.HttpBasicAuthenticationExchangeFilterFunction;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class WebClientFactory {

  private final ApolloClientAuthenticationPropertiesFactory apolloClientAuthenticationPropertiesFactory;

  private final ApolloClientAuthorizedClientManagerFactory apolloClientAuthorizedClientManagerFactory;

  private final ApolloClientReactiveAuthorizedClientManagerFactory apolloClientReactiveAuthorizedClientManagerFactory;

  public WebClientFactory() {
    this.apolloClientAuthenticationPropertiesFactory = new ApolloClientAuthenticationPropertiesFactory();
    this.apolloClientAuthorizedClientManagerFactory = new ApolloClientAuthorizedClientManagerFactory();
    this.apolloClientReactiveAuthorizedClientManagerFactory = new ApolloClientReactiveAuthorizedClientManagerFactory();
  }

  public WebClient createWebClient(Binder binder, BindHandler bindHandler) {
    ApolloClientAuthenticationProperties properties = this.apolloClientAuthenticationPropertiesFactory
        .createApolloClientAuthenticationProperties(binder, bindHandler);
    if (properties == null || !properties.authenticationEnabled()) {
      return WebClient.create();
    }
    properties.validate();
    if (properties.getOauth2() != null && properties.getOauth2().getEnabled()) {
      return this.createOauth2WebClient(properties, binder, bindHandler);
    }
    return this.createHttpBasicWebClient(properties, binder, bindHandler);
  }

  private WebClient createOauth2WebClient(ApolloClientAuthenticationProperties properties,
      Binder binder,
      BindHandler bindHandler) {
    OAuth2ClientProperties oauth2ClientProperties = this.apolloClientAuthenticationPropertiesFactory
        .createOauth2ClientProperties(binder, bindHandler);
    if (oauth2ClientProperties == null) {
      throw new IllegalArgumentException("oauth2ClientProperties must not be null");
    }
    WebApplicationType webApplicationType = WebApplicationTypeUtil.deduceFromClasspath();
    if (WebApplicationType.REACTIVE.equals(webApplicationType)) {
      return this
          .getReactiveOauth2WebClient(oauth2ClientProperties, properties, binder, bindHandler);
    }
    return this.getOauth2WebClient(oauth2ClientProperties, properties, binder, bindHandler);
  }

  /**
   * reactive oauth2 authentication webclient
   */
  private WebClient getReactiveOauth2WebClient(OAuth2ClientProperties oauth2ClientProperties,
      ApolloClientAuthenticationProperties properties, Binder binder,
      BindHandler bindHandler) {
    ReactiveOAuth2AuthorizedClientManager authorizedClientManager = this.apolloClientReactiveAuthorizedClientManagerFactory
        .createAuthorizedClientManager(oauth2ClientProperties);
    return this.reactiveOauth2WebClient(authorizedClientManager, properties);
  }

  private WebClient reactiveOauth2WebClient(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    Oauth2ReactiveAuthenticationWebClientCustomizer customizer = this
        .reactiveOauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder.build();
  }

  private Oauth2ReactiveAuthenticationWebClientCustomizer reactiveOauth2AuthenticationWebClientCustomizer(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction =
        new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    filterFunction.setDefaultOAuth2AuthorizedClient(true);
    filterFunction
        .setDefaultClientRegistrationId(properties.getOauth2().getDefaultClientRegistrationId());
    return new Oauth2ReactiveAuthenticationWebClientCustomizer(filterFunction);
  }

  /**
   * oauth2 authentication webclient
   */
  private WebClient getOauth2WebClient(OAuth2ClientProperties oauth2ClientProperties,
      ApolloClientAuthenticationProperties properties, Binder binder,
      BindHandler bindHandler) {
    OAuth2AuthorizedClientManager authorizedClientManager = this.apolloClientAuthorizedClientManagerFactory
        .createAuthorizedClientManager(oauth2ClientProperties);
    return this.oauth2WebClient(authorizedClientManager, properties);
  }

  private WebClient oauth2WebClient(OAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    Oauth2AuthenticationWebClientCustomizer customizer = this
        .oauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder.build();
  }

  private Oauth2AuthenticationWebClientCustomizer oauth2AuthenticationWebClientCustomizer(
      OAuth2AuthorizedClientManager authorizedClientManager,
      ApolloClientAuthenticationProperties properties) {
    ServletOAuth2AuthorizedClientExchangeFilterFunction filterFunction =
        new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    filterFunction.setDefaultOAuth2AuthorizedClient(true);
    filterFunction
        .setDefaultClientRegistrationId(properties.getOauth2().getDefaultClientRegistrationId());
    return new Oauth2AuthenticationWebClientCustomizer(filterFunction);
  }

  /**
   * http basic authentication webclient
   */
  private WebClient createHttpBasicWebClient(ApolloClientAuthenticationProperties properties,
      Binder binder,
      BindHandler bindHandler) {
    properties.getHttpBasic().validate();
    return this.httpBasicWebClient(properties);
  }

  private WebClient httpBasicWebClient(ApolloClientAuthenticationProperties properties) {
    HttpBasicAuthenticationWebClientCustomizer customizer = this
        .httpBasicAuthenticationWebClientCustomizer(properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder.build();
  }

  private HttpBasicAuthenticationWebClientCustomizer httpBasicAuthenticationWebClientCustomizer(
      ApolloClientAuthenticationProperties properties) {
    ApolloClientHttpBasicAuthenticationProperties httpBasic = properties.getHttpBasic();
    if (httpBasic.validateUsernameAndPassword()) {
      return new HttpBasicAuthenticationWebClientCustomizer(
          new HttpBasicAuthenticationExchangeFilterFunction(httpBasic.getUsername(),
              httpBasic.getPassword()));
    }
    return new HttpBasicAuthenticationWebClientCustomizer(
        new HttpBasicAuthenticationExchangeFilterFunction(httpBasic.getEncodedCredentials()));
  }
}
