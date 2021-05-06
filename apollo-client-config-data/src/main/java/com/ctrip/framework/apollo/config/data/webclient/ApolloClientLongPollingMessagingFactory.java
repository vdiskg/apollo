package com.ctrip.framework.apollo.config.data.webclient;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.webclient.injector.ApolloClientCustomHttpClientInjectorCustomizer;
import org.apache.commons.logging.Log;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientLongPollingMessagingFactory {

  private final Log log;

  private final ApolloClientWebClientFactory apolloClientWebClientFactory;

  public ApolloClientLongPollingMessagingFactory(Log log) {
    this.log = log;
    this.apolloClientWebClientFactory = new ApolloClientWebClientFactory(log);
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
