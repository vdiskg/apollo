/*
 * Copyright 2021 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.ctrip.framework.apollo.config.data.importer;

import com.ctrip.framework.apollo.config.data.extension.messaging.ApolloClientExtensionMessagingFactory;
import com.ctrip.framework.apollo.config.data.system.ApolloClientSystemPropertyInitializer;
import com.ctrip.framework.apollo.core.utils.DeferredLogger;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloConfigDataLoaderInitializer {

  public static volatile boolean INITIALIZED = false;

  private final Log log;

  private final Binder binder;

  private final BindHandler bindHandler;

  private final ConfigurableBootstrapContext bootstrapContext;

  public ApolloConfigDataLoaderInitializer(Log log,
      Binder binder, BindHandler bindHandler,
      ConfigurableBootstrapContext bootstrapContext) {
    this.log = log;
    this.binder = binder;
    this.bindHandler = bindHandler;
    this.bootstrapContext = bootstrapContext;
  }

  public void initApolloClient() {
    if (INITIALIZED) {
      return;
    }
    synchronized (ApolloConfigDataLoaderInitializer.class) {
      if (INITIALIZED) {
        return;
      }
      this.initApolloClientInternal();
      INITIALIZED = true;
    }
  }

  private void initApolloClientInternal() {
    new ApolloClientSystemPropertyInitializer(this.log)
        .initializeSystemProperty(this.binder, this.bindHandler);
    new ApolloClientExtensionMessagingFactory(this.log,
        this.bootstrapContext).prepareMessaging(this.binder, this.bindHandler);
    DeferredLogger.enable();
  }
}
