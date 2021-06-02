package com.ctrip.framework.apollo.config.data.extension.webclient;

import com.ctrip.framework.apollo.config.data.extension.messaging.ApolloClientPropertiesFactory;
import com.ctrip.framework.apollo.config.data.extension.properties.ApolloClientProperties;
import com.ctrip.framework.apollo.config.data.extension.webclient.injector.ApolloClientCustomHttpClientInjectorCustomizer;
import com.ctrip.framework.apollo.exceptions.ApolloConfigStatusCodeException;
import com.ctrip.framework.apollo.util.http.HttpClient;
import com.ctrip.framework.apollo.util.http.HttpRequest;
import com.ctrip.framework.apollo.util.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApolloClientWebClientTestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-authentication")
public class ApolloClientWebClientTest {

  @LocalServerPort
  private int serverPort;

  @Test
  public void testHttpBasicSuccess() {
    HttpClient httpClient = this.createHttpClient("test", "test-pw");
    String data = String.valueOf(ThreadLocalRandom.current().nextLong());
    HttpRequest httpRequest = new HttpRequest(
        "http://localhost:" + this.serverPort + "/test/echo/" + data);
    HttpResponse<String> httpResponse = httpClient.doGet(httpRequest, String.class);

    Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatusCode());
    Assert.assertEquals(data, httpResponse.getBody());
  }

  private HttpClient createHttpClient(String username, String password) {
    ApolloClientLongPollingMessagingFactory factory = new ApolloClientLongPollingMessagingFactory(
        LogFactory.getLog(ApolloClientWebClientTest.class), null);
    Map<String, String> map = new LinkedHashMap<>();
    map.put("apollo.client.extension.authentication.authentication-type", "http_basic");
    map.put("apollo.client.extension.authentication.http-basic.username", username);
    map.put("apollo.client.extension.authentication.http-basic.password", password);
    map.put("apollo.client.extension.enabled", "true");
    map.put("apollo.client.extension.messaging-type", "long_polling");
    MapConfigurationPropertySource propertySource = new MapConfigurationPropertySource(map);
    Binder binder = new Binder(propertySource);
    ApolloClientProperties apolloClientProperties = new ApolloClientPropertiesFactory()
        .createApolloClientProperties(binder, null);
    factory.prepareMessaging(apolloClientProperties, binder, null);
    ApolloClientCustomHttpClientInjectorCustomizer customizer = new ApolloClientCustomHttpClientInjectorCustomizer();
    return customizer.getInstance(HttpClient.class);
  }

  @Test(expected = ApolloConfigStatusCodeException.class)
  public void testHttpBasicFailed() {
    HttpClient httpClient = this.createHttpClient("test", "wrong-pw");
    String data = String.valueOf(ThreadLocalRandom.current().nextLong());
    HttpRequest httpRequest = new HttpRequest(
        "http://localhost:" + this.serverPort + "/test/echo/" + data);
    try {
      httpClient.doGet(httpRequest, String.class);
    } catch (ApolloConfigStatusCodeException e) {
      Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), e.getStatusCode());
      throw e;
    }
  }
}
