package com.ctrip.framework.apollo.config.data.webclient.customizer;

import com.ctrip.framework.apollo.config.data.webclient.filter.ApolloClientHttpBasicAuthenticationExchangeFilterFunction;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientHttpBasicAuthenticationWebClientCustomizer implements WebClientCustomizer {

    private final ApolloClientHttpBasicAuthenticationExchangeFilterFunction filterFunction;

    public ApolloClientHttpBasicAuthenticationWebClientCustomizer(
        ApolloClientHttpBasicAuthenticationExchangeFilterFunction filterFunction) {
        this.filterFunction = filterFunction;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter(this.filterFunction);
    }
}
