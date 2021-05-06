package com.ctrip.framework.apollo.config.data.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class LogFormatter {

  /**
   * format log message
   *
   * @param pattern slf4j log message patten
   * @param args    log message args
   * @return string
   */
  public static String format(String pattern, Object... args) {
    return MessageFormatter.arrayFormat(pattern, args, null).getMessage();
  }
}
