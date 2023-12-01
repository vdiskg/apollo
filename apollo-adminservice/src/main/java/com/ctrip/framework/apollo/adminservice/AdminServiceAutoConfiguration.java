/*
 * Copyright 2023 Apollo Authors
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
package com.ctrip.framework.apollo.adminservice;

import com.ctrip.framework.apollo.adminservice.filter.AdminServiceAuthenticationFilter;
import com.ctrip.framework.apollo.biz.config.BizConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class AdminServiceAutoConfiguration {

  private final BizConfig bizConfig;

  public AdminServiceAutoConfiguration(final BizConfig bizConfig) {
    this.bizConfig = bizConfig;
  }

  @Bean
  public FilterRegistrationBean<AdminServiceAuthenticationFilter> adminServiceAuthenticationFilter() {
    FilterRegistrationBean<AdminServiceAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();

    filterRegistrationBean.setFilter(new AdminServiceAuthenticationFilter(bizConfig));
    filterRegistrationBean.addUrlPatterns("/apps/*");
    filterRegistrationBean.addUrlPatterns("/appnamespaces/*");
    filterRegistrationBean.addUrlPatterns("/instances/*");
    filterRegistrationBean.addUrlPatterns("/items/*");
    filterRegistrationBean.addUrlPatterns("/namespaces/*");
    filterRegistrationBean.addUrlPatterns("/releases/*");

    return filterRegistrationBean;
  }

  /**
   * for apollo-assembly
   */
  @Order(99)
  @Configuration
  static class AdminServiceSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
      http.httpBasic();
    }
  }
}
