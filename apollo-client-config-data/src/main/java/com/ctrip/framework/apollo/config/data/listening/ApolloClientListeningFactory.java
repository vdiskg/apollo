package com.ctrip.framework.apollo.config.data.listening;

import com.ctrip.framework.apollo.config.data.authentication.ApolloClientPropertiesFactory;
import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.enums.ApolloClientListeningType;
import com.ctrip.framework.apollo.config.data.webclient.ApolloClientLongPollingListeningFactory;
import com.ctrip.framework.apollo.config.data.websocket.ApolloClientWebsocketListeningFactory;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientListeningFactory {

  private final ApolloClientPropertiesFactory apolloClientPropertiesFactory;

  private final ApolloClientLongPollingListeningFactory apolloClientLongPollingListeningFactory;

  private final ApolloClientWebsocketListeningFactory apolloClientWebsocketListeningFactory;

  public ApolloClientListeningFactory() {
    this.apolloClientPropertiesFactory = new ApolloClientPropertiesFactory();
    this.apolloClientLongPollingListeningFactory = new ApolloClientLongPollingListeningFactory();
    this.apolloClientWebsocketListeningFactory = new ApolloClientWebsocketListeningFactory();
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
    ApolloClientListeningType listeningType = apolloClientProperties.getListeningType();
    switch (listeningType) {
      case LONG_POLLING:
        this.apolloClientLongPollingListeningFactory
            .prepareCustomListening(apolloClientProperties, binder, bindHandler);
        return;
      case WEBSOCKET:
        this.apolloClientWebsocketListeningFactory
            .prepareCustomListening(apolloClientProperties, binder, bindHandler);
        return;
      default:
        throw new IllegalStateException("Unexpected value: " + listeningType);
    }
  }
}
