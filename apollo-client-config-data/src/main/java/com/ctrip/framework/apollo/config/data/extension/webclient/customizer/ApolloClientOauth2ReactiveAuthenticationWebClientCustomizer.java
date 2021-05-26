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
package com.ctrip.framework.apollo.config.data.extension.webclient.customizer;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer implements
    WebClientCustomizer {

  private final ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction;

  public ApolloClientOauth2ReactiveAuthenticationWebClientCustomizer(
      ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction) {
    this.filterFunction = filterFunction;
  }

  @Override
  public void customize(WebClient.Builder webClientBuilder) {
    webClientBuilder.filter(this.filterFunction);
  }
}
