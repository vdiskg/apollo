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
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.util.StringUtils;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@EnableConfigurationProperties({OAuth2ClientProperties.class, OAuth2ResourceServerProperties.class})
@Profile("oauth2")
@Configuration(proxyBeanMethods = false)
public class ConfigServiceOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  private final List<ConfigServiceAuthorizeRequestsCustomizer> configServiceAuthorizeRequestsCustomizerList;

  private final InMemoryClientRegistrationRepository clientRegistrationRepository;

  private final OAuth2ResourceServerProperties oauth2ResourceServerProperties;

  public ConfigServiceOAuth2WebSecurityConfigurerAdapter(
      List<ConfigServiceAuthorizeRequestsCustomizer> configServiceAuthorizeRequestsCustomizerList,
      InMemoryClientRegistrationRepository clientRegistrationRepository,
      OAuth2ResourceServerProperties oauth2ResourceServerProperties) {
    this.configServiceAuthorizeRequestsCustomizerList = configServiceAuthorizeRequestsCustomizerList;
    this.clientRegistrationRepository = clientRegistrationRepository;
    this.oauth2ResourceServerProperties = oauth2ResourceServerProperties;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    for (ConfigServiceAuthorizeRequestsCustomizer customizer : this.configServiceAuthorizeRequestsCustomizerList) {
      http.authorizeRequests(customizer);
    }
    http.oauth2Login(configure ->
        configure.clientRegistrationRepository(
            new ExcludeClientCredentialsClientRegistrationRepository(
                this.clientRegistrationRepository)));
    http.oauth2Client();
    http.logout(configure -> {
      OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
          this.clientRegistrationRepository);
      logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
      configure.logoutSuccessHandler(logoutSuccessHandler);
    });
    String jwtIssuerUri = this.oauth2ResourceServerProperties.getJwt().getIssuerUri();
    if (!StringUtils.hasText(jwtIssuerUri)) {
      throw new IllegalArgumentException("apollo-config-service must configure the jwt issuerUri");
    }
    http.oauth2ResourceServer().jwt();
    http.csrf().disable();
  }
}
