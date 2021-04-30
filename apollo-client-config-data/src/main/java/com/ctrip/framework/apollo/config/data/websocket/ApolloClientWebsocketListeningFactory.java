package com.ctrip.framework.apollo.config.data.websocket;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientWebsocketListeningFactory {

  /**
   * prepare custom websocket listening
   *
   * @param apolloClientProperties apollo client custom properties
   * @param binder                 properties binder
   * @param bindHandler            properties bind handler
   */
  public void prepareCustomListening(ApolloClientProperties apolloClientProperties, Binder binder,
      BindHandler bindHandler) {
    throw new UnsupportedOperationException("not complete yet.");
  }
}
