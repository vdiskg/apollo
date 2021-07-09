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

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ProxyWebSocketHandler implements WebSocketHandler {

  private final Map<WebSocketSession, WebSocketSession> downstreamSessionMap = new ConcurrentHashMap<>();

  private final WebSocketClient webSocketClient;

  private final RSocketProperties rsocketProperties;

  public ProxyWebSocketHandler(WebSocketClient webSocketClient,
      RSocketProperties rsocketProperties) {
    this.webSocketClient = webSocketClient;
    this.rsocketProperties = rsocketProperties;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    Integer port = this.rsocketProperties.getServer().getPort();
    String mappingPath = this.rsocketProperties.getServer().getMappingPath();
    URI uri = URI.create("ws://localhost:" + port + mappingPath);
    WebSocketSession downstreamSession = this.webSocketClient
        .doHandshake(new DownstreamWebSocketHandler(session),
            new WebSocketHttpHeaders(session.getHandshakeHeaders()), uri).get();
    this.downstreamSessionMap.put(session, downstreamSession);
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    WebSocketSession downstreamSession = this.downstreamSessionMap.get(session);
    downstreamSession.sendMessage(message);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    WebSocketSession downstreamSession = this.downstreamSessionMap.remove(session);
    downstreamSession.close(closeStatus);
  }

  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
}
