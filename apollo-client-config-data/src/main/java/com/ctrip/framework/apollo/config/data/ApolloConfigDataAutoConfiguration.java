package com.ctrip.framework.apollo.config.data;

import com.ctrip.framework.apollo.config.data.authentication.ApolloClientAuthenticationPropertiesFactory;
import com.ctrip.framework.apollo.config.data.authentication.properties.ApolloClientAuthenticationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Configuration(proxyBeanMethods = false)
public class ApolloConfigDataAutoConfiguration {

  @ConditionalOnMissingBean(ApolloClientAuthenticationProperties.class)
  @ConfigurationProperties(ApolloClientAuthenticationPropertiesFactory.AUTHENTICATION_PROPERTIES_PREFIX)
  @Bean
  public static ApolloClientAuthenticationProperties apolloWebClientSecurityProperties() {
    return new ApolloClientAuthenticationProperties();
  }
}
