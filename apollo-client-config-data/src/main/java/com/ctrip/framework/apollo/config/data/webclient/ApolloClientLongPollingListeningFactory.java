package com.ctrip.framework.apollo.config.data.webclient;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.webclient.injector.ApolloClientCustomHttpClientInjectorCustomizer;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientLongPollingListeningFactory {

  private final ApolloClientWebClientFactory apolloClientWebClientFactory;

  public ApolloClientLongPollingListeningFactory() {
    this.apolloClientWebClientFactory = new ApolloClientWebClientFactory();
  }

  /**
   * prepare custom long polling listening
   *
   * @param apolloClientProperties apollo client custom properties
   * @param binder                 properties binder
   * @param bindHandler            properties bind handler
   */
  public void prepareCustomListening(ApolloClientProperties apolloClientProperties, Binder binder,
      BindHandler bindHandler) {
    WebClient webClient = this.apolloClientWebClientFactory
        .createWebClient(apolloClientProperties, binder, bindHandler);
    ApolloClientCustomHttpClientInjectorCustomizer.setCustomWebClient(webClient);
  }
}
