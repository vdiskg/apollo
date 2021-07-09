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
public class DownstreamWebSocketHandler implements WebSocketHandler {

  private final WebSocketSession upstreamSession;

  public DownstreamWebSocketHandler(WebSocketSession upstreamSession) {
    this.upstreamSession = upstreamSession;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {

  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    this.upstreamSession.sendMessage(message);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    this.upstreamSession.close(closeStatus);
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
}
