package com.ctrip.framework.apollo.config.data.extension.webclient;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@Configuration
@EnableAutoConfiguration
public class ApolloClientWebClientTestConfiguration extends WebSecurityConfigurerAdapter {

  @RequestMapping("/test")
  @RestController
  public static class TestController {

    @GetMapping("/echo/{data}")
    public String echo(@PathVariable String data) {
      return data;
    }
  }
}
