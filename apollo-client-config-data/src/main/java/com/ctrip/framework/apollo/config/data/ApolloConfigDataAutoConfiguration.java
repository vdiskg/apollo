package com.ctrip.framework.apollo.config.data;

import com.ctrip.framework.apollo.config.data.extension.messaging.ApolloClientPropertiesFactory;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Configuration(proxyBeanMethods = false)
public class ApolloConfigDataAutoConfiguration {

  @ConditionalOnMissingBean(ApolloClientProperties.class)
  @ConfigurationProperties(ApolloClientPropertiesFactory.PROPERTIES_PREFIX)
  @Bean
  public static ApolloClientProperties apolloWebClientSecurityProperties() {
    return new ApolloClientProperties();
  }
}
