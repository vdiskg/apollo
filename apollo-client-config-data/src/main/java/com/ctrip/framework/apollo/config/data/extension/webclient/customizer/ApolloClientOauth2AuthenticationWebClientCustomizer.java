package com.ctrip.framework.apollo.config.data.extension.webclient.customizer;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientOauth2AuthenticationWebClientCustomizer implements WebClientCustomizer {

  private final ServletOAuth2AuthorizedClientExchangeFilterFunction filterFunction;

  public ApolloClientOauth2AuthenticationWebClientCustomizer(
      ServletOAuth2AuthorizedClientExchangeFilterFunction filterFunction) {
    this.filterFunction = filterFunction;
  }

  @Override
  public void customize(WebClient.Builder webClientBuilder) {
    webClientBuilder.apply(this.filterFunction.oauth2Configuration());
  }
}
