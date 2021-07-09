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

import com.ctrip.framework.apollo.biz.entity.ReleaseMessage;
import com.ctrip.framework.apollo.biz.message.ReleaseMessageListener;
import com.ctrip.framework.apollo.core.dto.ApolloConfigNotification;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Controller
public class NotificationMessagingController implements ReleaseMessageListener {

  private final Map<String, FluxSinkContext<List<ApolloConfigNotification>>> contextMap = new ConcurrentHashMap<>();

  @MessageMapping("/notifications")
  public Flux<List<ApolloConfigNotification>> notifications() {
    String fluxSinkId = "";
    return Flux.<List<ApolloConfigNotification>>create(fluxSink -> {
          FluxSinkContext<List<ApolloConfigNotification>> old = this.contextMap.put(fluxSinkId, new FluxSinkContext<>(fluxSinkId, fluxSink));
          if (old != null) {
            old.close();
          }
        }, FluxSink.OverflowStrategy.LATEST)
        .doFinally(signalType -> this.contextMap.remove(fluxSinkId));
  }

  @Override
  public void handleMessage(ReleaseMessage message, String channel) {

  }
}
