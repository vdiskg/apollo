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
   * cluster name
   */
  public static final String APOLLO_CLUSTER = ConfigConsts.APOLLO_CLUSTER_KEY;

  /**
   * local cache directory
   */
  public static final String APOLLO_CACHE_DIR = "apollo.cache-dir";

  /**
   * apollo client access key
   */
  public static final String APOLLO_ACCESS_KEY_SECRET = "apollo.access-key.secret";

  /**
   * apollo meta server address
   */
  public static final String APOLLO_META = ConfigConsts.APOLLO_META_KEY;

  /**
   * apollo config service address
   */
  public static final String APOLLO_CONFIG_SERVICE = "apollo.config-service";

  /**
   * enable property order
   */
  public static final String APOLLO_PROPERTY_ORDER_ENABLE = "apollo.property.order.enable";
}
