package com.ctrip.framework.apollo.config.data.http;

import com.ctrip.framework.apollo.config.data.http.customizer.HttpBasicAuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.http.customizer.Oauth2AuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.http.customizer.Oauth2ReactiveAuthenticationWebClientCustomizer;
import com.ctrip.framework.apollo.config.data.http.filter.HttpBasicAuthenticationExchangeFilterFunction;
import com.ctrip.framework.apollo.config.data.http.properties.ApolloWebClientSecurityProperties;
import com.ctrip.framework.apollo.config.data.http.properties.ApolloWebClientHttpBasicAuthenticationProperties;
import com.ctrip.framework.apollo.config.data.http.util.WebApplicationTypeUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class WebClientFactory {

  public WebClient createWebClient(Binder binder, BindHandler bindHandler) {
    Boolean webclientAuthenticationEnabled = binder
        .bind("apollo.client.webclient.authentication.enabled", Bindable.of(Boolean.class),
            bindHandler)
        .orElse(false);
    if (!webclientAuthenticationEnabled) {
      return WebClient.create();
    }
    ApolloWebClientSecurityProperties properties = this.webClientSecurityProperties(binder, bindHandler);
    if (properties == null) {
      throw new IllegalArgumentException("webClientSecurityProperties must not be null");
    }
    properties.validate();
    if (properties.getOauth2() != null && properties.getOauth2().getEnabled()) {
      return this.createOauth2WebClient(properties, binder, bindHandler);
    }
    if (properties.getHttpBasic() != null && properties.getHttpBasic().getEnabled()) {
      return this.createHttpBasicWebClient(properties, binder, bindHandler);
    }
    throw new IllegalArgumentException("no authentication configured, should not enable the 'apollo.client.webclient.authentication.enabled'");
  }

  private ApolloWebClientSecurityProperties webClientSecurityProperties(Binder binder,
      BindHandler bindHandler) {
    return binder.bind("apollo.client.security", Bindable.of(ApolloWebClientSecurityProperties.class),
        bindHandler).orElse(null);
  }

  private WebClient createOauth2WebClient(ApolloWebClientSecurityProperties properties, Binder binder,
      BindHandler bindHandler) {
    OAuth2ClientProperties oauth2ClientProperties = this
        .oauth2ClientProperties(binder, bindHandler);
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

  private OAuth2ClientProperties oauth2ClientProperties(Binder binder, BindHandler bindHandler) {
    return binder.bind("spring.security.oauth2.client", Bindable.of(OAuth2ClientProperties.class),
        bindHandler).orElse(null);
  }

  /**
   * reactive oauth2 authentication webclient
   */
  private WebClient getReactiveOauth2WebClient(OAuth2ClientProperties oauth2ClientProperties,
      ApolloWebClientSecurityProperties properties, Binder binder,
      BindHandler bindHandler) {
    ReactiveClientRegistrationRepository reactiveClientRegistrationRepository = this
        .reactiveClientRegistrationRepository(oauth2ClientProperties);
    ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService = this
        .reactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository);
    ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager = this
        .reactiveOAuth2AuthorizedClientManager(reactiveClientRegistrationRepository,
            reactiveOAuth2AuthorizedClientService);
    return this.reactiveOauth2WebClient(reactiveOAuth2AuthorizedClientManager, properties);
  }

  private ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(
      OAuth2ClientProperties oauth2ClientProperties) {
    List<ClientRegistration> registrations = new ArrayList<>(
        OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oauth2ClientProperties)
            .values());
    return new InMemoryReactiveClientRegistrationRepository(registrations);
  }

  private ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService(
      ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
    return new InMemoryReactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository);
  }

  private ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {
    ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
        ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .build();

    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
        new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientService);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
    return authorizedClientManager;
  }

  private WebClient reactiveOauth2WebClient(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      ApolloWebClientSecurityProperties properties) {
    Oauth2ReactiveAuthenticationWebClientCustomizer customizer = this
        .reactiveOauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder.build();
  }

  private Oauth2ReactiveAuthenticationWebClientCustomizer reactiveOauth2AuthenticationWebClientCustomizer(
      ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
      ApolloWebClientSecurityProperties properties) {
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
      ApolloWebClientSecurityProperties properties, Binder binder,
      BindHandler bindHandler) {
    ClientRegistrationRepository clientRegistrationRepository = this
        .clientRegistrationRepository(oauth2ClientProperties);
    OAuth2AuthorizedClientService authorizedClientService = this
        .authorizedClientService(clientRegistrationRepository);
    OAuth2AuthorizedClientManager authorizedClientManager = this
        .authorizedClientManager(clientRegistrationRepository, authorizedClientService);
    return this.oauth2WebClient(authorizedClientManager, properties);
  }

  private ClientRegistrationRepository clientRegistrationRepository(
      OAuth2ClientProperties oauth2ClientProperties) {
    List<ClientRegistration> registrations = new ArrayList<>(
        OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oauth2ClientProperties)
            .values());
    return new InMemoryClientRegistrationRepository(registrations);
  }

  private OAuth2AuthorizedClientService authorizedClientService(
      ClientRegistrationRepository clientRegistrationRepository) {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
  }

  private OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientService authorizedClientService) {
    OAuth2AuthorizedClientProvider authorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .build();
    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
        new AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientService);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
    return authorizedClientManager;
  }

  private WebClient oauth2WebClient(OAuth2AuthorizedClientManager authorizedClientManager,
      ApolloWebClientSecurityProperties properties) {
    Oauth2AuthenticationWebClientCustomizer customizer = this
        .oauth2AuthenticationWebClientCustomizer(authorizedClientManager, properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder.build();
  }

  private Oauth2AuthenticationWebClientCustomizer oauth2AuthenticationWebClientCustomizer(
      OAuth2AuthorizedClientManager authorizedClientManager,
      ApolloWebClientSecurityProperties properties) {
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
  private WebClient createHttpBasicWebClient(ApolloWebClientSecurityProperties properties, Binder binder,
      BindHandler bindHandler) {
    properties.getHttpBasic().validate();
    return this.httpBasicWebClient(properties);
  }

  private WebClient httpBasicWebClient(ApolloWebClientSecurityProperties properties) {
    HttpBasicAuthenticationWebClientCustomizer customizer = this
        .httpBasicAuthenticationWebClientCustomizer(properties);
    WebClient.Builder webClientBuilder = WebClient.builder();
    customizer.customize(webClientBuilder);
    return webClientBuilder.build();
  }

  private HttpBasicAuthenticationWebClientCustomizer httpBasicAuthenticationWebClientCustomizer(
      ApolloWebClientSecurityProperties properties) {
    ApolloWebClientHttpBasicAuthenticationProperties httpBasic = properties.getHttpBasic();
    if (httpBasic.validateUsernameAndPassword()) {
      return new HttpBasicAuthenticationWebClientCustomizer(
          new HttpBasicAuthenticationExchangeFilterFunction(httpBasic.getUsername(),
              httpBasic.getPassword()));
    }
    return new HttpBasicAuthenticationWebClientCustomizer(
        new HttpBasicAuthenticationExchangeFilterFunction(httpBasic.getEncodedCredentials()));
  }
}
