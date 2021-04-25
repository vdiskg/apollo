package com.ctrip.framework.apollo.config.data.authentication.oauth2;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientReactiveAuthorizedClientManagerFactory {

  public ReactiveOAuth2AuthorizedClientManager createAuthorizedClientManager(
      OAuth2ClientProperties oauth2ClientProperties) {
    ReactiveClientRegistrationRepository reactiveClientRegistrationRepository = this
        .clientRegistrationRepository(oauth2ClientProperties);
    ReactiveOAuth2AuthorizedClientService authorizedClientService = this
        .authorizedClientService(reactiveClientRegistrationRepository);
    return this.authorizedClientManager(reactiveClientRegistrationRepository,
        authorizedClientService);
  }

  private ReactiveClientRegistrationRepository clientRegistrationRepository(
      OAuth2ClientProperties oauth2ClientProperties) {
    List<ClientRegistration> registrations = new ArrayList<>(
        OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oauth2ClientProperties)
            .values());
    return new InMemoryReactiveClientRegistrationRepository(registrations);
  }

  private ReactiveOAuth2AuthorizedClientService authorizedClientService(
      ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
    return new InMemoryReactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository);
  }

  private ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
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
}
