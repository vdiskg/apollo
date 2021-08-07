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
package com.ctrip.framework.apollo.internals;

import com.ctrip.framework.apollo.util.ApolloHashMapInitialUtil;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultConfig extends AbstractRepositoryConfig implements RepositoryChangeListener {

  /**
   * Constructor.
   *
   * @param namespace        the namespace of this config instance
   * @param configRepository the config repository for this config instance
   */
  public DefaultConfig(String namespace, ConfigRepository configRepository) {
    super(namespace, configRepository);
  }

  @Override
  public String getProperty(String key, String defaultValue) {
    // step 1: check system properties, i.e. -Dkey=value
    String value = System.getProperty(key);

    // step 2: check local cached properties file
    if (value == null) {
      value = this.getPropertyFromRepository(key);
    }

    /*
     * step 3: check env variable, i.e. PATH=...
     * normally system environment variables are in UPPERCASE, however there might be exceptions.
     * so the caller should provide the key in the right case
     */
    if (value == null) {
      value = System.getenv(key);
    }

    // step 4: check properties file from classpath
    if (value == null) {
      value = this.getPropertyFromAdditional(key);
    }

    this.tryWarnLog(value);

    return value == null ? defaultValue : value;
  }

  @Override
  public Set<String> getPropertyNames() {
    Set<String> fromRepository = this.getPropertyNamesFromRepository();
    Set<String> fromAdditional = this.getPropertyNamesFromAdditional();
    Set<String> fromSystemProperty = this.stringPropertyNames(System.getProperties());
    Set<String> fromEnv = System.getenv().keySet();
    int initialCapacity = ApolloHashMapInitialUtil.getInitialCapacity(
        fromRepository.size() + fromAdditional.size() + fromSystemProperty.size() + fromEnv.size());
    Set<String> propertyNames = new HashSet<>(initialCapacity);
    propertyNames.addAll(fromRepository);
    propertyNames.addAll(fromAdditional);
    propertyNames.addAll(fromSystemProperty);
    propertyNames.addAll(fromEnv);
    return propertyNames;
  }
}
