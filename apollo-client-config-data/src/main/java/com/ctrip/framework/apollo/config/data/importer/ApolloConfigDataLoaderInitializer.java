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

import com.ctrip.framework.apollo.config.data.extension.initialize.ApolloClientExtensionInitializeFactory;
import com.ctrip.framework.apollo.config.data.system.ApolloClientSystemPropertyInitializer;
import com.ctrip.framework.apollo.core.utils.DeferredLogger;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

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

  /**
   * init apollo client (only once)
   *
   * @return initial sources as placeholders or empty list if already initialized
   */
  public List<PropertySource<?>> initApolloClient() {
    if (INITIALIZED) {
      return Collections.emptyList();
    }
    synchronized (ApolloConfigDataLoaderInitializer.class) {
      if (INITIALIZED) {
        return Collections.emptyList();
      }
      this.initApolloClientInternal();
      INITIALIZED = true;
      // force disable apollo bootstrap to avoid conflict
      Map<String, Object> map = new HashMap<>();
      map.put(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, "false");
      map.put(PropertySourcesConstants.APOLLO_BOOTSTRAP_EAGER_LOAD_ENABLED, "false");
      // provide initial sources as placeholders to avoid duplicate loading
      return Arrays.asList(
          new ApolloConfigEmptyPropertySource(PropertySourcesConstants.APOLLO_PROPERTY_SOURCE_NAME),
          new MapPropertySource(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME,
              map));
    }
  }

  private void initApolloClientInternal() {
    new ApolloClientSystemPropertyInitializer(this.log)
        .initializeSystemProperty(this.binder, this.bindHandler);
    new ApolloClientExtensionInitializeFactory(this.log,
        this.bootstrapContext).initializeExtension(this.binder, this.bindHandler);
    DeferredLogger.enable();
  }
}
