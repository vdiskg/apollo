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

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Profile("http-basic")
@Configuration(proxyBeanMethods = false)
public class ConfigServiceHttpBasicWebSecurityConfigurerAdapter extends
    WebSecurityConfigurerAdapter {

  private final List<ConfigServiceAuthorizeRequestsCustomizer> configServiceAuthorizeRequestsCustomizerList;

  public ConfigServiceHttpBasicWebSecurityConfigurerAdapter(
      List<ConfigServiceAuthorizeRequestsCustomizer> configServiceAuthorizeRequestsCustomizerList) {
    this.configServiceAuthorizeRequestsCustomizerList = configServiceAuthorizeRequestsCustomizerList;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    for (ConfigServiceAuthorizeRequestsCustomizer customizer : this.configServiceAuthorizeRequestsCustomizerList) {
      http.authorizeRequests(customizer);
    }
    http.httpBasic();
    http.csrf().disable();
  }
}
