package com.ctrip.framework.apollo.config.data.extension.properties;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientExtensionProperties {

  /**
   * enable apollo client extension(webclient/websocket and authentication)
   */
  private Boolean enabled = false;

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "ApolloClientExtensionProperties{" +
        "enabled=" + enabled +
        '}';
  }
}
