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

import reactor.core.publisher.FluxSink;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class FluxSinkContext<E> {

  private final String fluxSinkId;

  private final FluxSink<E> fluxSink;

  public FluxSinkContext(String fluxSinkId, FluxSink<E> fluxSink) {
    this.fluxSinkId = fluxSinkId;
    this.fluxSink = fluxSink;
  }

  public void send(E message) {
    this.fluxSink.next(message);
  }

  public void close() {
    this.fluxSink.complete();
  }

  public String getFluxSinkId() {
    return fluxSinkId;
  }

  public FluxSink<E> getFluxSink() {
    return fluxSink;
  }

  @Override
  public String toString() {
    return "FluxSinkContext{" +
        "fluxSinkId='" + fluxSinkId + '\'' +
        ", fluxSink=" + fluxSink +
        '}';
  }
}
