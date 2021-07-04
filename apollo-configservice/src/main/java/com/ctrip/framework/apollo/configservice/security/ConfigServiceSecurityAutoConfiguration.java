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
package com.ctrip.framework.apollo.configservice.security;

import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Import({ConfigServiceHttpBasicWebSecurityConfigurerAdapter.class,
    ConfigServiceOAuth2WebSecurityConfigurerAdapter.class})
@Configuration(proxyBeanMethods = false)
public class ConfigServiceSecurityAutoConfiguration {

  @Order(Ordered.LOWEST_PRECEDENCE - 100)
  @Bean
  public static ConfigServiceAuthorizeRequestsCustomizer configServiceAuthorizeRequestsCustomizer(
      EurekaInstanceConfigBean eurekaInstanceConfigBean) {
    return requests -> {
      requests.antMatchers("/configs/**", "/notifications/**",
          eurekaInstanceConfigBean.getHealthCheckUrlPath()).permitAll();
      requests.anyRequest().authenticated();
    };
  }
}
