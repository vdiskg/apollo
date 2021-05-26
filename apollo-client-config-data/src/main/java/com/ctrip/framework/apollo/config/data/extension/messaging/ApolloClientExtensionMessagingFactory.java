package com.ctrip.framework.apollo.config.data.extension.messaging;

import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.extension.enums.ApolloClientMessagingType;
import com.ctrip.framework.apollo.config.data.extension.webclient.ApolloClientLongPollingMessagingFactory;
import com.ctrip.framework.apollo.config.data.extension.websocket.ApolloClientWebsocketMessagingFactory;
import com.ctrip.framework.apollo.config.data.util.Slf4jLogMessageFormatter;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientExtensionMessagingFactory {

  private final Log log;

  private final ApolloClientPropertiesFactory apolloClientPropertiesFactory;

  private final ApolloClientLongPollingMessagingFactory apolloClientLongPollingMessagingFactory;

  private final ApolloClientWebsocketMessagingFactory apolloClientWebsocketMessagingFactory;

  public ApolloClientExtensionMessagingFactory(Log log,
      ConfigurableBootstrapContext bootstrapContext) {
    this.log = log;
    this.apolloClientPropertiesFactory = new ApolloClientPropertiesFactory();
    this.apolloClientLongPollingMessagingFactory = new ApolloClientLongPollingMessagingFactory(log,
        bootstrapContext);
    this.apolloClientWebsocketMessagingFactory = new ApolloClientWebsocketMessagingFactory(log,
        bootstrapContext);
  }

  /**
   * prepare extension messaging
   *
   * @param binder      properties binder
   * @param bindHandler properties bind handler
   */
  public void prepareMessaging(Binder binder, BindHandler bindHandler) {
    ApolloClientProperties apolloClientProperties = this.apolloClientPropertiesFactory
        .createApolloClientProperties(binder, bindHandler);
    if (apolloClientProperties == null || apolloClientProperties.getExtension() == null) {
      this.log.info("apollo client extension is not configured, default to disabled");
      return;
    }
    if (!apolloClientProperties.getExtension().getEnabled()) {
      this.log.info("apollo client extension disabled");
      return;
    }
    ApolloClientMessagingType messagingType = apolloClientProperties.getMessagingType();
    log.debug(Slf4jLogMessageFormatter
        .format("apollo client extension messaging type: {}", messagingType));
    switch (messagingType) {
      case LONG_POLLING:
        this.apolloClientLongPollingMessagingFactory
            .prepareMessaging(apolloClientProperties, binder, bindHandler);
        return;
      case WEBSOCKET:
        this.apolloClientWebsocketMessagingFactory
            .prepareMessaging(apolloClientProperties, binder, bindHandler);
        return;
      default:
        throw new IllegalStateException("Unexpected value: " + messagingType);
    }
  }
}
