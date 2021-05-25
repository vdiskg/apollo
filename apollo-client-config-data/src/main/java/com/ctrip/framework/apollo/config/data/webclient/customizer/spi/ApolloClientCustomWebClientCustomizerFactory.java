package com.ctrip.framework.apollo.config.data.webclient.customizer.spi;

import com.ctrip.framework.apollo.core.spi.Ordered;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.lang.Nullable;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public interface ApolloClientCustomWebClientCustomizerFactory extends Ordered {

  /**
   * create a WebClientCustomizer instance
   *
   * @param binder           properties binder
   * @param bindHandler      properties binder Handler
   * @param log              deferred log
   * @param bootstrapContext bootstrapContext
   * @return WebClientCustomizer instance or null
   */
  @Nullable
  WebClientCustomizer createWebClientCustomizer(Binder binder, BindHandler bindHandler, Log log,
      ConfigurableBootstrapContext bootstrapContext);
}
