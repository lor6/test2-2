package com.baeldung.reactive.logging;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.baeldung.reactive.logging.filters.LogFilters;
import com.baeldung.reactive.logging.jetty.RequestLogEnhancer;
import com.baeldung.reactive.logging.netty.CustomLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WebClientLoggingIntegrationTest {

    @AllArgsConstructor
    @Data
    class Post {
        private String title;
        private String body;
        private int userId;
    }

    private Appender jettyAppender;
    private Appender nettyAppender;
    private Appender mockAppender;
    private String sampleUrl = "https://jsonplaceholder.typicode.com/posts";

    private Post post;
    private String sampleResponseBody;

    @BeforeEach
    private void setup() throws Exception {

        post = new Post("Learn WebClient logging with Baeldung!", "", 1);
        sampleResponseBody = new ObjectMapper().writeValueAsString(post);

        ch.qos.logback.classic.Logger jetty = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.baeldung.reactive.logging.jetty");
        jettyAppender = mock(Appender.class);
        when(jettyAppender.getName()).thenReturn("com.baeldung.reactive.logging.jetty");
        jetty.addAppender(jettyAppender);

        ch.qos.logback.classic.Logger netty = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("reactor.netty.http.client");
        nettyAppender = mock(Appender.class);
        when(nettyAppender.getName()).thenReturn("reactor.netty.http.client");
        netty.addAppender(nettyAppender);

        ch.qos.logback.classic.Logger test = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.baeldung.reactive");
        mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("com.baeldung.reactive");
        test.addAppender(mockAppender);

    }

    @Test
    public void givenJettyHttpClient_whenEndpointIsConsumed_thenRequestAndResponseBodyShouldBeLogged() {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        org.eclipse.jetty.client.HttpClient httpClient = new org.eclipse.jetty.client.HttpClient(sslContextFactory) {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return new RequestLogEnhancer().enhance(request);
            }
        };

        WebClient
          .builder()
          .clientConnector(new JettyClientHttpConnector(httpClient))
          .build()
          .post()
          .uri(sampleUrl)
          .body(BodyInserters.fromObject(post))
          .retrieve()
          .bodyToMono(String.class)
          .block();

        verify(jettyAppender).doAppend(argThat(argument -> (((LoggingEvent) argument).getFormattedMessage()).contains(sampleResponseBody)));
    }

    @Test
    public void givenNettyHttpClientWithWiretap_whenEndpointIsConsumed_thenRequestAndResponseBodyShouldBeLogged() {

        reactor.netty.http.client.HttpClient httpClient = HttpClient
          .create()
          .wiretap(true);
        WebClient
          .builder()
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .build()
          .post()
          .uri(sampleUrl)
          .body(BodyInserters.fromObject(post))
          .exchange()
          .block();
        verify(nettyAppender).doAppend(argThat(argument -> (((LoggingEvent) argument).getFormattedMessage()).contains("00000300")));
    }

    @Test
    public void givenNettyHttpClientWithCustomLogger_whenEndpointIsConsumed_thenRequestAndResponseBodyShouldBeLogged() {

        reactor.netty.http.client.HttpClient httpClient = HttpClient
          .create()
          .tcpConfiguration(
            tc -> tc.bootstrap(
              b -> BootstrapHandlers.updateLogSupport(b, new CustomLogger(HttpClient.class))));
        WebClient
          .builder()
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .build()
          .post()
          .uri(sampleUrl)
          .body(BodyInserters.fromObject(post))
          .exchange()
          .block();
        verify(nettyAppender).doAppend(argThat(argument -> (((LoggingEvent) argument).getFormattedMessage()).contains(sampleResponseBody)));
    }

    @Test
    public void givenDefaultHttpClientWithString_whenEndpointIsConsumed_thenRequestAndResponseLogged() {
        WebClient
          .builder()
          .filters(exchangeFilterFunctions -> {
              exchangeFilterFunctions.addAll(LogFilters.prepareFilters());
          })
          .build()
          .post()
          .uri(sampleUrl)
          .body(BodyInserters.fromObject(post))
          .exchange()
          .block();

        verify(mockAppender).doAppend(argThat(argument -> (((LoggingEvent) argument).getFormattedMessage()).contains("domain=.typicode.com;")));
    }


}
