package com.ctrip.framework.apollo.config.data.util;

import org.springframework.boot.WebApplicationType;
import org.springframework.util.ClassUtils;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class WebApplicationTypeUtil {

  private static final String[] SERVLET_INDICATOR_CLASSES = {"javax.servlet.Servlet",
      "org.springframework.web.context.ConfigurableWebApplicationContext"};

  private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

  private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

  private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

  private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";

  private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

  /**
   * deduce WebApplicationType {@link WebApplicationType#deduceFromClasspath()}
   *
   * @return WebApplicationType
   */
  public static WebApplicationType deduceFromClasspath() {
    if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils
        .isPresent(WEBMVC_INDICATOR_CLASS, null)
        && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
      return WebApplicationType.REACTIVE;
    }
    for (String className : SERVLET_INDICATOR_CLASSES) {
      if (!ClassUtils.isPresent(className, null)) {
        return WebApplicationType.NONE;
      }
    }
    return WebApplicationType.SERVLET;
  }
}
