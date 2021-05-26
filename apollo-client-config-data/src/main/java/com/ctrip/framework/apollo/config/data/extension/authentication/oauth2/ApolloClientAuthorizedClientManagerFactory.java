package com.ctrip.framework.apollo.config.data.extension.authentication.oauth2;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientAuthorizedClientManagerFactory {

  public OAuth2AuthorizedClientManager createAuthorizedClientManager(
      OAuth2ClientProperties oauth2ClientProperties) {
    ClientRegistrationRepository clientRegistrationRepository = this
        .clientRegistrationRepository(oauth2ClientProperties);
    OAuth2AuthorizedClientService authorizedClientService = this
        .authorizedClientService(clientRegistrationRepository);
    return this.authorizedClientManager(clientRegistrationRepository, authorizedClientService);
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
}
