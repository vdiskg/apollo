package com.ctrip.framework.apollo.config.data;

import com.ctrip.framework.apollo.config.data.http.properties.ApolloWebClientSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Configuration(proxyBeanMethods = false)
public class ApolloConfigDataAutoConfiguration {

  @ConditionalOnMissingBean(ApolloWebClientSecurityProperties.class)
  @ConfigurationProperties("apollo.client.security")
  @Bean
  public static ApolloWebClientSecurityProperties apolloWebClientSecurityProperties() {
    return new ApolloWebClientSecurityProperties();
  }
}
