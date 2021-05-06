package com.ctrip.framework.apollo.config.data.messaging;

import com.ctrip.framework.apollo.config.data.authentication.ApolloClientPropertiesFactory;
import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.enums.ApolloClientMessagingType;
import com.ctrip.framework.apollo.config.data.webclient.ApolloClientLongPollingMessagingFactory;
import com.ctrip.framework.apollo.config.data.websocket.ApolloClientWebsocketMessagingFactory;
import org.apache.commons.logging.Log;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientMessagingFactory {

  private final Log log;

  private final ApolloClientPropertiesFactory apolloClientPropertiesFactory;

  private final ApolloClientLongPollingMessagingFactory apolloClientLongPollingMessagingFactory;

  private final ApolloClientWebsocketMessagingFactory apolloClientWebsocketMessagingFactory;

  public ApolloClientMessagingFactory(Log log) {
    this.log = log;
    this.apolloClientPropertiesFactory = new ApolloClientPropertiesFactory();
    this.apolloClientLongPollingMessagingFactory = new ApolloClientLongPollingMessagingFactory(log);
    this.apolloClientWebsocketMessagingFactory = new ApolloClientWebsocketMessagingFactory(log);
  }

  /**
   * prepare custom messaging
   *
   * @param binder      properties binder
   * @param bindHandler properties bind handler
   */
  public void prepareCustomMessaging(Binder binder, BindHandler bindHandler) {
    ApolloClientProperties apolloClientProperties = this.apolloClientPropertiesFactory
        .createApolloClientProperties(binder, bindHandler);
    if (apolloClientProperties == null) {
      this.log.info("apollo client custom messaging disabled");
      return;
    }
    ApolloClientMessagingType messagingType = apolloClientProperties.getMessagingType();
    switch (messagingType) {
      case LONG_POLLING:
        this.log.debug("apollo client custom long polling messaging enabled");
        this.apolloClientLongPollingMessagingFactory
            .prepareCustomListening(apolloClientProperties, binder, bindHandler);
        return;
      case WEBSOCKET:
        this.log.debug("apollo client custom websocket messaging enabled");
        this.apolloClientWebsocketMessagingFactory
            .prepareCustomListening(apolloClientProperties, binder, bindHandler);
        return;
      default:
        throw new IllegalStateException("Unexpected value: " + messagingType);
    }
  }
}
