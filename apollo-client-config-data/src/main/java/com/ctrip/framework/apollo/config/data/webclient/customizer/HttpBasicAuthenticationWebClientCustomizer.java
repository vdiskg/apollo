package com.ctrip.framework.apollo.config.data.webclient.customizer;

import com.ctrip.framework.apollo.config.data.webclient.filter.HttpBasicAuthenticationExchangeFilterFunction;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class HttpBasicAuthenticationWebClientCustomizer implements WebClientCustomizer {

    private final HttpBasicAuthenticationExchangeFilterFunction filterFunction;

    public HttpBasicAuthenticationWebClientCustomizer(HttpBasicAuthenticationExchangeFilterFunction filterFunction) {
        this.filterFunction = filterFunction;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter(this.filterFunction);
    }
}
