package com.ctrip.framework.apollo.config.data.importer;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.config.data.http.WebClientFactory;
import com.ctrip.framework.apollo.config.data.injector.CustomHttpClientInjectorCustomizer;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySource;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySourceFactory;
import com.ctrip.framework.apollo.util.factory.PropertiesFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloConfigDataLoader implements ConfigDataLoader<ApolloConfigDataResource>, Ordered {

  private final WebClientFactory webClientFactory = new WebClientFactory();

  /**
   * {@link com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer#initialize(org.springframework.core.env.ConfigurableEnvironment)}
   */
  @Override
  public ConfigData load(ConfigDataLoaderContext context, ApolloConfigDataResource resource)
      throws IOException, ConfigDataResourceNotFoundException {
    Binder binder = context.getBootstrapContext().get(Binder.class);
    BindHandler bindHandler = this.getBindHandler(context);
    this.setSystemProperties(binder, bindHandler);
    WebClient webClient = this.webClientFactory.createWebClient(binder, bindHandler);
    CustomHttpClientInjectorCustomizer.setCustomWebClient(webClient);
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

  private void setSystemProperties(Binder binder, BindHandler bindHandler) {
    Boolean propertyOrderEnable = binder
        .bind(PropertiesFactory.APOLLO_PROPERTY_ORDER_ENABLE, Bindable.of(Boolean.class),
            bindHandler)
        .orElse(false);
    System.setProperty(PropertiesFactory.APOLLO_PROPERTY_ORDER_ENABLE,
        propertyOrderEnable.toString());
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 100;
  }
}
