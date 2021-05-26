package com.ctrip.framework.apollo.config.data.extension.webclient;

import com.ctrip.framework.apollo.exceptions.ApolloConfigException;
import com.ctrip.framework.apollo.exceptions.ApolloConfigStatusCodeException;
import com.ctrip.framework.apollo.util.http.HttpClient;
import com.ctrip.framework.apollo.util.http.HttpRequest;
import com.ctrip.framework.apollo.util.http.HttpResponse;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloWebClientHttpClient implements HttpClient {

  private final WebClient webClient;

  private final Gson gson;

  public ApolloWebClientHttpClient(WebClient webClient) {
    this(webClient, new Gson());
  }

  public ApolloWebClientHttpClient(WebClient webClient, Gson gson) {
    this.webClient = webClient;
    this.gson = gson;
  }

  @Override
  public <T> HttpResponse<T> doGet(HttpRequest httpRequest, Class<T> responseType)
      throws ApolloConfigException {
    return this.doGetInternal(httpRequest, responseType);
  }

  private <T> HttpResponse<T> doGetInternal(HttpRequest httpRequest, Type responseType)
      throws ApolloConfigException {
    WebClient.RequestHeadersSpec<?> requestHeadersSpec = this.webClient.get()
        .uri(URI.create(httpRequest.getUrl()));
    if (!CollectionUtils.isEmpty(httpRequest.getHeaders())) {
      for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
        requestHeadersSpec.header(entry.getKey(), entry.getValue());
      }
    }
    return requestHeadersSpec.exchangeToMono(clientResponse -> {
      if (HttpStatus.OK.equals(clientResponse.statusCode())) {
        return clientResponse.bodyToMono(String.class)
            .map(body -> new HttpResponse<T>(HttpStatus.OK.value(),
                gson.fromJson(body, responseType)));
      }
      if (HttpStatus.NOT_MODIFIED.equals(clientResponse.statusCode())) {
        return Mono.just(new HttpResponse<T>(HttpStatus.NOT_MODIFIED.value(), null));
      }
      return Mono.error(new ApolloConfigStatusCodeException(clientResponse.rawStatusCode(),
          String.format("Get operation failed for %s", httpRequest.getUrl())));
    }).block();
  }

  @Override
  public <T> HttpResponse<T> doGet(HttpRequest httpRequest, Type responseType)
      throws ApolloConfigException {
    return this.doGetInternal(httpRequest, responseType);
  }
}
