/*
 * Copyright 2021 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.ctrip.framework.apollo.config.data.extension.webclient;

import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.extension.messaging.ApolloClientMessagingFactory;
import com.ctrip.framework.apollo.config.data.extension.webclient.customizer.spi.ApolloClientCustomWebClientCustomizerFactory;
import com.ctrip.framework.apollo.config.data.extension.webclient.injector.ApolloClientCustomHttpClientInjectorCustomizer;
import com.ctrip.framework.foundation.internals.ServiceBootstrap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientLongPollingMessagingFactory implements ApolloClientMessagingFactory {

  private final Log log;

  private final ConfigurableBootstrapContext bootstrapContext;

  private final ApolloClientWebClientFactory apolloClientWebClientFactory;

  public ApolloClientLongPollingMessagingFactory(Log log,
      ConfigurableBootstrapContext bootstrapContext) {
    this.log = log;
    this.bootstrapContext = bootstrapContext;
    this.apolloClientWebClientFactory = new ApolloClientWebClientFactory(log);
  }

  @Override
  public void prepareMessaging(ApolloClientProperties apolloClientProperties, Binder binder,
      BindHandler bindHandler) {
    WebClient.Builder webClientBuilder = this.apolloClientWebClientFactory
        .createWebClient(apolloClientProperties, binder, bindHandler);
    List<ApolloClientCustomWebClientCustomizerFactory> factories = ServiceBootstrap
        .loadAllOrdered(ApolloClientCustomWebClientCustomizerFactory.class);
    if (!CollectionUtils.isEmpty(factories)) {
      for (ApolloClientCustomWebClientCustomizerFactory factory : factories) {
        WebClientCustomizer webClientCustomizer = factory
            .createWebClientCustomizer(binder, bindHandler, this.log, this.bootstrapContext);
        if (webClientCustomizer != null) {
          webClientCustomizer.customize(webClientBuilder);
        }
      }
    }
    ApolloClientCustomHttpClientInjectorCustomizer.setCustomWebClient(webClientBuilder.build());
  }
}
