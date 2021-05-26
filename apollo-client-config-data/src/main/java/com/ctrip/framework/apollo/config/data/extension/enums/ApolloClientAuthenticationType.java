package com.ctrip.framework.apollo.config.data.extension.enums;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public enum ApolloClientAuthenticationType {

  /**
   * disable authentication
   */
  NONE,

  /**
   * enable oauth2 authentication
   */
  OAUTH2,

  /**
   * enable http-basic authentication
   */
  HTTP_BASIC,
  ;
}
