package com.ctrip.framework.apollo.config.data.listener;

import com.ctrip.framework.apollo.core.utils.DeferredLogger;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloDeferredLoggerApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent> {

  @Override
  public void onApplicationEvent(ApplicationContextInitializedEvent event) {
    DeferredLogger.replayTo();
  }
}
