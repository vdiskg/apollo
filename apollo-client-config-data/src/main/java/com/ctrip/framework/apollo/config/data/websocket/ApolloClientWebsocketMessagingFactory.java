package com.ctrip.framework.apollo.config.data.websocket;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.messaging.ApolloClientMessagingFactory;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientWebsocketMessagingFactory implements ApolloClientMessagingFactory {

  private final Log log;

  private final ConfigurableBootstrapContext bootstrapContext;

  public ApolloClientWebsocketMessagingFactory(Log log,
      ConfigurableBootstrapContext bootstrapContext) {
    this.log = log;
    this.bootstrapContext = bootstrapContext;
  }

  @Override
  public void prepareMessaging(ApolloClientProperties apolloClientProperties, Binder binder,
      BindHandler bindHandler) {
    throw new UnsupportedOperationException("not complete yet.");
  }
}
