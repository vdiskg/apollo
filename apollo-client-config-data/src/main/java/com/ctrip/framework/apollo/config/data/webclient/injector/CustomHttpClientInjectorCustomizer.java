package com.ctrip.framework.apollo.config.data.webclient.injector;

import com.ctrip.framework.apollo.config.data.webclient.WebClientHttpClient;
import com.ctrip.framework.apollo.core.spi.Ordered;
import com.ctrip.framework.apollo.spi.ApolloInjectorCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class CustomHttpClientInjectorCustomizer implements ApolloInjectorCustomizer {

  /**
   * the order of the injector customizer
   */
  public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 100;

  private static WebClientHttpClient CUSTOM_HTTP_CLIENT;

  /**
   * set the webClient to use
   *
   * @param webClient webClient to use
   */
  public static void setCustomWebClient(WebClient webClient) {
    CUSTOM_HTTP_CLIENT = new WebClientHttpClient(webClient);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getInstance(Class<T> clazz) {
    if (clazz.isInstance(CUSTOM_HTTP_CLIENT)) {
      return (T) CUSTOM_HTTP_CLIENT;
    }
    return null;
  }

  @Override
  public <T> T getInstance(Class<T> clazz, String name) {
    return null;
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
