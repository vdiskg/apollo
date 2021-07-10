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
package com.ctrip.framework.apollo.configservice.websocket;

import reactor.core.publisher.MonoSink;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class MonoSinkContext<E> {

  private final String monoSinkId;

  private final MonoSink<E> monoSink;

  public MonoSinkContext(String monoSinkId, MonoSink<E> monoSink) {
    this.monoSinkId = monoSinkId;
    this.monoSink = monoSink;
  }

  public void send(E message) {
    this.monoSink.success(message);
  }

  public void close() {
    this.monoSink.success();
  }

  public String getMonoSinkId() {
    return monoSinkId;
  }

  public MonoSink<E> getMonoSink() {
    return monoSink;
  }

  @Override
  public String toString() {
    return "MonoSinkContext{" +
        "monoSinkId='" + monoSinkId + '\'' +
        ", monoSink=" + monoSink +
        '}';
  }
}
