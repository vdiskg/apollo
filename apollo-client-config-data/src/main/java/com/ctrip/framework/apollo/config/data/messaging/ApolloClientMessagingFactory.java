package com.ctrip.framework.apollo.config.data.messaging;

import com.ctrip.framework.apollo.config.data.authentication.ApolloClientPropertiesFactory;
import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.enums.ApolloClientMessagingType;
import com.ctrip.framework.apollo.config.data.webclient.ApolloClientLongPollingMessagingFactory;
import com.ctrip.framework.apollo.config.data.websocket.ApolloClientWebsocketMessagingFactory;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientMessagingFactory {

  private final ApolloClientPropertiesFactory apolloClientPropertiesFactory;

  private final ApolloClientLongPollingMessagingFactory apolloClientLongPollingMessagingFactory;

  private final ApolloClientWebsocketMessagingFactory apolloClientWebsocketMessagingFactory;

  public ApolloClientMessagingFactory() {
    this.apolloClientPropertiesFactory = new ApolloClientPropertiesFactory();
    this.apolloClientLongPollingMessagingFactory = new ApolloClientLongPollingMessagingFactory();
    this.apolloClientWebsocketMessagingFactory = new ApolloClientWebsocketMessagingFactory();
  }

  /**
   * prepare custom listening
   *
   * @param binder      properties binder
   * @param bindHandler properties bind handler
   */
  public void prepareCustomListening(Binder binder, BindHandler bindHandler) {
    ApolloClientProperties apolloClientProperties = this.apolloClientPropertiesFactory
        .createApolloClientProperties(binder, bindHandler);
    if (apolloClientProperties == null) {
      return;
    }
    ApolloClientMessagingType messagingType = apolloClientProperties.getMessagingType();
    switch (messagingType) {
      case LONG_POLLING:
        this.apolloClientLongPollingMessagingFactory
            .prepareCustomListening(apolloClientProperties, binder, bindHandler);
        return;
      case WEBSOCKET:
        this.apolloClientWebsocketMessagingFactory
            .prepareCustomListening(apolloClientProperties, binder, bindHandler);
        return;
      default:
        throw new IllegalStateException("Unexpected value: " + messagingType);
    }
  }
}
