package com.ctrip.framework.apollo.config.data.system;

import com.ctrip.framework.apollo.core.ApolloClientSystemConsts;
import com.ctrip.framework.apollo.spring.boot.ApolloApplicationContextInitializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApolloClientPropertyCompatibleTestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test-compatible")
public class ApolloClientApplicationPropertiesCompatibleTest {

  @Autowired
  private ConfigurableEnvironment environment;

  @Test
  public void testApplicationPropertiesCompatible() {
    Assert.assertEquals("test-1/cacheDir",
        this.environment.getProperty(ApolloClientSystemConsts.APOLLO_CACHE_DIR));
    Assert.assertEquals("test-1-secret",
        this.environment.getProperty(ApolloClientSystemConsts.APOLLO_ACCESS_KEY_SECRET));
    Assert.assertEquals("https://test-1-config-service",
        this.environment.getProperty(ApolloClientSystemConsts.APOLLO_CONFIG_SERVICE));
  }

  @After
  public void clearProperty() {
    for (String propertyName : ApolloApplicationContextInitializer.APOLLO_SYSTEM_PROPERTIES) {
      System.clearProperty(propertyName);
    }
  }
}
