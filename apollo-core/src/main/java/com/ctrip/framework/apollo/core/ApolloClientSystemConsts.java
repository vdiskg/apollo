package com.ctrip.framework.apollo.core;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientSystemConsts {

  /**
   * apollo client app id
   */
  public static final String APP_ID = "app.id";

  /**
   * apollo client app id environment variables
   */
  public static final String APP_ID_ENVIRONMENT_VARIABLES = "APP_ID";

  /**
   * cluster name
   */
  public static final String APOLLO_CLUSTER = ConfigConsts.APOLLO_CLUSTER_KEY;

  /**
   * cluster name environment variables
   */
  public static final String APOLLO_CLUSTER_ENVIRONMENT_VARIABLES = "APOLLO_CLUSTER";

  /**
   * local cache directory
   */
  public static final String APOLLO_CACHE_DIR = "apollo.cache-dir";

  /**
   * local cache directory environment variables
   */
  public static final String APOLLO_CACHE_DIR_ENVIRONMENT_VARIABLES = "APOLLO_CACHE_DIR";

  /**
   * apollo client access key
   */
  public static final String APOLLO_ACCESS_KEY_SECRET = "apollo.access-key.secret";

  /**
   * apollo client access key environment variables
   */
  public static final String APOLLO_ACCESS_KEY_SECRET_ENVIRONMENT_VARIABLES = "APOLLO_ACCESS_KEY_SECRET";

  /**
   * apollo meta server address
   */
  public static final String APOLLO_META = ConfigConsts.APOLLO_META_KEY;

  /**
   * apollo meta server address environment variables
   */
  public static final String APOLLO_META_ENVIRONMENT_VARIABLES = "APOLLO_META";

  /**
   * apollo config service address
   */
  public static final String APOLLO_CONFIG_SERVICE = "apollo.config-service";

  /**
   * apollo config service address environment variables
   */
  public static final String APOLLO_CONFIG_SERVICE_ENVIRONMENT_VARIABLES = "APOLLO_CONFIG_SERVICE";

  /**
   * enable property order
   */
  public static final String APOLLO_PROPERTY_ORDER_ENABLE = "apollo.property.order.enable";

  /**
   * enable property order environment variables
   */
  public static final String APOLLO_PROPERTY_ORDER_ENABLE_ENVIRONMENT_VARIABLES = "APOLLO_PROPERTY_ORDER_ENABLE";
}
