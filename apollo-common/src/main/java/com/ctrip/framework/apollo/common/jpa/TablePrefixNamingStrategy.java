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
package com.ctrip.framework.apollo.common.jpa;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class TablePrefixNamingStrategy extends PhysicalNamingStrategyStandardImpl implements
    PhysicalNamingStrategy {

  private static final long serialVersionUID = -5268252502936563292L;

  private final String tablePrefix;

  public TablePrefixNamingStrategy(String tablePrefix) {
    this.tablePrefix = tablePrefix;
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    String tablePrefix = this.tablePrefix;
    if (StringUtils.isEmpty(tablePrefix)) {
      return name;
    }
    String entityTableName = name.getText();
    String physicalTableName = tablePrefix + entityTableName;
    return new Identifier(physicalTableName, name.isQuoted());
  }
}
