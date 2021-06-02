package com.ctrip.framework.apollo.config.data.system;

import com.ctrip.framework.apollo.core.ApolloClientSystemConsts;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApolloClientPropertyCompatibleTestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApolloClientEnvironmentVariablesCompatibleTest {

  @Autowired
  private ConfigurableEnvironment environment;

  @Test
  public void testEnvironmentVariablesCompatible() throws Exception {
    SystemLambda.withEnvironmentVariable("APOLLO_CACHEDIR", "test-2/cacheDir")
        .and("APOLLO_ACCESSKEY_SECRET", "test-2-secret")
        .and("APOLLO_CONFIGSERVICE", "https://test-2-config-service")
        .execute(() -> {
          Assert.assertEquals("test-2/cacheDir",
              this.environment.getProperty(ApolloClientSystemConsts.APOLLO_CACHE_DIR));
          Assert.assertEquals("test-2-secret",
              this.environment.getProperty(ApolloClientSystemConsts.APOLLO_ACCESS_KEY_SECRET));
          Assert.assertEquals("https://test-2-config-service",
              this.environment.getProperty(ApolloClientSystemConsts.APOLLO_CONFIG_SERVICE));
        });
  }
}
