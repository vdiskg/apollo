package com.ctrip.framework.apollo.config.data.webclient.customizer;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer implements WebClientCustomizer {

    private final ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction;

    public ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer(ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction) {
        this.filterFunction = filterFunction;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter(this.filterFunction);
    }
}
