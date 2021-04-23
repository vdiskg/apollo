package com.ctrip.framework.apollo.config.data.importer;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.config.Profiles;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.Ordered;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloConfigDataLocationResolver implements
    ConfigDataLocationResolver<ApolloConfigDataResource>, Ordered {

  private static final String PREFIX = "apollo:";

  @Override
  public boolean isResolvable(ConfigDataLocationResolverContext context,
      ConfigDataLocation location) {
    return location.hasPrefix(PREFIX);
  }

  @Override
  public List<ApolloConfigDataResource> resolve(ConfigDataLocationResolverContext context,
      ConfigDataLocation location)
      throws ConfigDataLocationNotFoundException, ConfigDataResourceNotFoundException {
    return Collections.emptyList();
  }

  @Override
  public List<ApolloConfigDataResource> resolveProfileSpecific(
      ConfigDataLocationResolverContext context, ConfigDataLocation location, Profiles profiles)
      throws ConfigDataLocationNotFoundException {
    Binder binder = context.getBinder();
    BindHandler bindHandler = this.getBindHandler(context);
    String namespaces = binder
        .bind(PropertySourcesConstants.APOLLO_BOOTSTRAP_NAMESPACES, Bindable.of(String.class),
            bindHandler)
        .orElse(ConfigConsts.NAMESPACE_APPLICATION);
    String[] namespaceArray = namespaces.split(",");
    if (namespaceArray.length == 0) {
      return Collections.emptyList();
    }
    return Collections.singletonList(new ApolloConfigDataResource(Arrays.asList(namespaceArray)));
  }

  private BindHandler getBindHandler(ConfigDataLocationResolverContext context) {
    return context.getBootstrapContext().getOrElse(BindHandler.class, null);
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
