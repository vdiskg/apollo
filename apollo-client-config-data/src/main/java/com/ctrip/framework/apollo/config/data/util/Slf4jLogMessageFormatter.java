package com.ctrip.framework.apollo.config.data.util;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.core.log.LogMessage;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class Slf4jLogMessageFormatter {

  /**
   * format log message
   *
   * @param pattern slf4j log message patten
   * @param args    log message args
   * @return string
   */
  public static LogMessage format(String pattern, Object... args) {
    return LogMessage.of(() -> MessageFormatter.arrayFormat(pattern, args, null).getMessage());
  }
}
