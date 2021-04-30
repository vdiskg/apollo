package com.ctrip.framework.apollo.config.data.properties;

import com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.util.StringUtils;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientSystemPropertyProcessor {

  public void setSystemProperties(Binder binder, BindHandler bindHandler) {
    for (String propertyName : ApolloApplicationContextInitializer.APOLLO_SYSTEM_PROPERTIES) {
      this.fillSystemPropertyFromBinder(propertyName, binder, bindHandler);
    }
  }

  private void fillSystemPropertyFromBinder(String propertyName, Binder binder,
      BindHandler bindHandler) {
    if (System.getProperty(propertyName) != null) {
      return;
    }
    String propertyValue = binder.bind(propertyName, Bindable.of(String.class), bindHandler)
        .orElse(null);
    if (!StringUtils.hasText(propertyValue)) {
      return;
    }
    System.setProperty(propertyName, propertyValue);
  }
}
