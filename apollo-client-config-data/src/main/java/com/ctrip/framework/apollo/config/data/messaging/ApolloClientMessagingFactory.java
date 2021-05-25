package com.ctrip.framework.apollo.config.data.messaging;

import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public interface ApolloClientMessagingFactory {

  /**
   * prepare extension messaging
   *
   * @param apolloClientProperties apollo client extension properties
   * @param binder                 properties binder
   * @param bindHandler            properties bind handler
   */
  void prepareMessaging(ApolloClientProperties apolloClientProperties, Binder binder,
      BindHandler bindHandler);
}
