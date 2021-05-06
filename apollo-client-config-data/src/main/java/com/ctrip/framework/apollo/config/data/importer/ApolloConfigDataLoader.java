package com.ctrip.framework.apollo.config.data.importer;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.config.data.messaging.ApolloClientMessagingFactory;
import com.ctrip.framework.apollo.config.data.properties.ApolloClientSystemPropertyProcessor;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySource;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySourceFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.Ordered;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloConfigDataLoader implements ConfigDataLoader<ApolloConfigDataResource>, Ordered {

  private final ApolloClientSystemPropertyProcessor apolloClientSystemPropertyProcessor = new ApolloClientSystemPropertyProcessor();

  private final ApolloClientMessagingFactory apolloClientMessagingFactory = new ApolloClientMessagingFactory();

  /**
   * {@link com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer#initialize(org.springframework.core.env.ConfigurableEnvironment)}
   */
  @Override
  public ConfigData load(ConfigDataLoaderContext context, ApolloConfigDataResource resource)
      throws IOException, ConfigDataResourceNotFoundException {
    Binder binder = context.getBootstrapContext().get(Binder.class);
    BindHandler bindHandler = this.getBindHandler(context);
    this.apolloClientSystemPropertyProcessor.setSystemProperties(binder, bindHandler);
    this.apolloClientMessagingFactory.prepareCustomListening(binder, bindHandler);
    context.getBootstrapContext().registerIfAbsent(ConfigPropertySourceFactory.class,
        BootstrapRegistry.InstanceSupplier.from(ConfigPropertySourceFactory::new));
    ConfigPropertySourceFactory configPropertySourceFactory = context.getBootstrapContext()
        .get(ConfigPropertySourceFactory.class);
    List<ConfigPropertySource> propertySources = new ArrayList<>(
        resource.getNamespaceList().size());
    for (String namespace : resource.getNamespaceList()) {
      Config config = ConfigService.getConfig(namespace);
      propertySources.add(configPropertySourceFactory.getConfigPropertySource(namespace, config));
    }
    return new ConfigData(propertySources);
  }

  private BindHandler getBindHandler(ConfigDataLoaderContext context) {
    return context.getBootstrapContext().getOrElse(BindHandler.class, null);
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 100;
  }
}
