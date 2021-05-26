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
package com.ctrip.framework.apollo.config.data.extension.properties;

import java.nio.charset.Charset;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientHttpBasicAuthenticationProperties implements InitializingBean {

  /**
   * http-basic authentication username
   *
   * @see HttpHeaders#encodeBasicAuth(String, String, Charset)
   * @see HttpHeaders#setBasicAuth(String)
   */
  private String username = "";

  /**
   * http-basic authentication password
   *
   * @see HttpHeaders#encodeBasicAuth(String, String, Charset)
   * @see HttpHeaders#setBasicAuth(String)
   */
  private String password = "";

  /**
   * http-basic authentication encodedCredentials
   *
   * @see HttpHeaders#setBasicAuth(String)
   */
  private String encodedCredentials = "";

  @Override
  public void afterPropertiesSet() {
    this.validate();
  }

  public void validate() {
    if (this.validateUsernameAndPassword() && this.validateEncodedCredentials()) {
      throw new IllegalStateException("duplicated username password pair && encodedCredentials");
    }
  }

  public boolean validateUsernameAndPassword() {
    return StringUtils.hasText(this.getUsername())
        && StringUtils.hasText(this.getPassword());
  }

  public boolean validateEncodedCredentials() {
    return StringUtils.hasText(this.getEncodedCredentials());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEncodedCredentials() {
    return encodedCredentials;
  }

  public void setEncodedCredentials(String encodedCredentials) {
    this.encodedCredentials = encodedCredentials;
  }

  @Override
  public String toString() {
    return "ApolloClientHttpBasicAuthenticationProperties{" +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", encodedCredentials='" + encodedCredentials + '\'' +
        '}';
  }
}
