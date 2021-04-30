package com.ctrip.framework.apollo.config.data.webclient.filter;

import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * to apply a given HTTP Basic Authentication username/password pair, unless a custom {@code
 * Authorization} header has already been set. {@link org.springframework.http.client.support.BasicAuthenticationInterceptor}
 *
 * @author vdisk <vdisk@foxmail.com>
 */
public class ApolloClientHttpBasicAuthenticationExchangeFilterFunction implements
    ExchangeFilterFunction {

  private final String encodedCredentials;

  /**
   * Create a new filter which adds Basic Authentication for the given username and password.
   *
   * @param username the username to use
   * @param password the password to use
   * @see HttpHeaders#encodeBasicAuth(String, String, Charset)
   * @see HttpHeaders#setBasicAuth(String)
   */
  public ApolloClientHttpBasicAuthenticationExchangeFilterFunction(String username,
      String password) {
    this(username, password, null);
  }

  /**
   * Create a new filter which adds Basic Authentication for the given username and password,
   * encoded using the specified charset.
   *
   * @param username the username to use
   * @param password the password to use
   * @param charset  the charset to use
   * @see HttpHeaders#encodeBasicAuth(String, String, Charset)
   * @see HttpHeaders#setBasicAuth(String)
   */
  public ApolloClientHttpBasicAuthenticationExchangeFilterFunction(String username, String password,
      @Nullable Charset charset) {
    this(HttpHeaders.encodeBasicAuth(username, password, charset));
  }

  /**
   * Create a new filter which adds Basic Authentication for the given encodedCredentials.
   *
   * @param encodedCredentials the encodedCredentials to use
   * @see HttpHeaders#setBasicAuth(String)
   */
  public ApolloClientHttpBasicAuthenticationExchangeFilterFunction(String encodedCredentials) {
    this.encodedCredentials = encodedCredentials;
  }

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    HttpHeaders headers = request.headers();
    if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
      headers.setBasicAuth(this.encodedCredentials);
    }
    return next.exchange(request);
  }
}
