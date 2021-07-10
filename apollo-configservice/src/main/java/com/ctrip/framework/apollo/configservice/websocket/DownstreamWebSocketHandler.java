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

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@FunctionalInterface
public interface DownstreamWebSocketHandler extends WebSocketHandler {

  /**
   * get upstream websocket session
   *
   * @return websocket session
   */
  WebSocketSession getUpstreamSession();

  @Override
  default void afterConnectionEstablished(WebSocketSession session) throws Exception {
  }

  @Override
  default void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    this.getUpstreamSession().sendMessage(message);
  }

  @Override
  default void handleTransportError(WebSocketSession session, Throwable exception)
      throws Exception {
  }

  @Override
  default void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    this.getUpstreamSession().close(closeStatus);
  }

  @Override
  default boolean supportsPartialMessages() {
    return false;
  }
}
