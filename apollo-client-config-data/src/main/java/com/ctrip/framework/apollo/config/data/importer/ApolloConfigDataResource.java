package com.ctrip.framework.apollo.config.data.importer;

import java.util.List;
import org.springframework.boot.context.config.ConfigDataResource;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloConfigDataResource extends ConfigDataResource {

  private final List<String> namespaceList;

  public ApolloConfigDataResource(List<String> namespaceList) {
    this.namespaceList = namespaceList;
  }

  public List<String> getNamespaceList() {
    return namespaceList;
  }

  @Override
  public String toString() {
    return "ApolloConfigDataResource{" +
        "namespaceList=" + namespaceList +
        '}';
  }
}
