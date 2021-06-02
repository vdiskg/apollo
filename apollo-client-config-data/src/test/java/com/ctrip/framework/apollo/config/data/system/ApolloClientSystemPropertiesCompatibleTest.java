package com.ctrip.framework.apollo.config.data.system;

import com.ctrip.framework.apollo.core.ApolloClientSystemConsts;
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
public class ApolloClientSystemPropertiesCompatibleTest {

  @Autowired
  private ConfigurableEnvironment environment;

  @Test
  public void testSystemPropertiesCompatible() {
    System.setProperty("apollo.cacheDir", "test-3/cacheDir");
    System.setProperty("apollo.accesskey.secret", "test-3-secret");
    System.setProperty("apollo.configService", "https://test-3-config-service");

    Assert.assertEquals("test-3/cacheDir",
        this.environment.getProperty(ApolloClientSystemConsts.APOLLO_CACHE_DIR));
    Assert.assertEquals("test-3-secret",
        this.environment.getProperty(ApolloClientSystemConsts.APOLLO_ACCESS_KEY_SECRET));
    Assert.assertEquals("https://test-3-config-service",
        this.environment.getProperty(ApolloClientSystemConsts.APOLLO_CONFIG_SERVICE));

    System.clearProperty("apollo.cacheDir");
    System.clearProperty("apollo.accesskey.secret");
    System.clearProperty("apollo.configService");
  }
}
