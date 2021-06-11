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

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.config.data.extension.messaging.ApolloClientExtensionMessagingFactory;
import com.ctrip.framework.apollo.config.data.system.ApolloClientSystemPropertyInitializer;
import com.ctrip.framework.apollo.config.data.util.Slf4jLogMessageFormatter;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySource;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySourceFactory;
import com.ctrip.framework.apollo.spring.util.SpringInjector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertySource;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloConfigDataLoader implements ConfigDataLoader<ApolloConfigDataResource>, Ordered {

  private final Log log;

  private final ConfigurableBootstrapContext bootstrapContext;

  private final ApolloClientSystemPropertyInitializer apolloClientSystemPropertyInitializer;

  private final ApolloClientExtensionMessagingFactory apolloClientExtensionMessagingFactory;

  public ApolloConfigDataLoader(Log log,
      ConfigurableBootstrapContext bootstrapContext) {
    this.log = log;
    this.bootstrapContext = bootstrapContext;
    this.apolloClientSystemPropertyInitializer = new ApolloClientSystemPropertyInitializer(log);
    this.apolloClientExtensionMessagingFactory = new ApolloClientExtensionMessagingFactory(log,
        bootstrapContext);
  }

  @Override
  public ConfigData load(ConfigDataLoaderContext context, ApolloConfigDataResource resource)
      throws IOException, ConfigDataResourceNotFoundException {
    Binder binder = context.getBootstrapContext().get(Binder.class);
    BindHandler bindHandler = this.getBindHandler(context);
    context.getBootstrapContext().registerIfAbsent(ApolloConfigDataLoaderInitializer.class,
        BootstrapRegistry.InstanceSupplier.from(
            () -> new ApolloConfigDataLoaderInitializer(this.log, binder, bindHandler,
                this.bootstrapContext)));
    ApolloConfigDataLoaderInitializer apolloConfigDataLoaderInitializer = context
        .getBootstrapContext()
        .get(ApolloConfigDataLoaderInitializer.class);
    // init apollo client
    List<EmptyPropertySource> emptyPropertySourceList = apolloConfigDataLoaderInitializer
        .initApolloClient();
    // load config
    context.getBootstrapContext().registerIfAbsent(ConfigPropertySourceFactory.class,
        BootstrapRegistry.InstanceSupplier
            .from(() -> SpringInjector.getInstance(ConfigPropertySourceFactory.class)));
    ConfigPropertySourceFactory configPropertySourceFactory = context.getBootstrapContext()
        .get(ConfigPropertySourceFactory.class);
    String namespace = resource.getNamespace();
    Config config = ConfigService.getConfig(namespace);
    ConfigPropertySource configPropertySource = configPropertySourceFactory
        .getConfigPropertySource(namespace, config);
    List<PropertySource<?>> propertySourceList = new ArrayList<>();
    propertySourceList.add(configPropertySource);
    propertySourceList.addAll(emptyPropertySourceList);
    log.debug(Slf4jLogMessageFormatter
        .format("apollo client loaded namespace [{}]", resource.getNamespace()));
    return new ConfigData(propertySourceList);
  }

  private BindHandler getBindHandler(ConfigDataLoaderContext context) {
    return context.getBootstrapContext().getOrElse(BindHandler.class, null);
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 100;
  }
}
