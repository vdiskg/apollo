package com.ctrip.framework.apollo.config.data.websocket;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import org.apache.commons.logging.Log;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientWebsocketMessagingFactory {

  private final Log log;

  public ApolloClientWebsocketMessagingFactory(Log log) {
    this.log = log;
  }

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
