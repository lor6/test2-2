package com.baeldung.web.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.baeldung.web.reactive.client.Foo;
import com.baeldung.web.reactive.client.WebClientApplication;

import io.netty.channel.ChannelOption;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@SpringBootTest(classes = WebClientApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebClientIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void givenDifferentScenarios_whenRequestsSent_thenObtainExpectedResponses() {
        // WebClient
        WebClient client1 = WebClient.create();
        WebClient client2 = WebClient.create("http://localhost:" + port);
        WebClient client3 = WebClient.builder()
            .baseUrl("http://localhost:" + port)
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();

        // request specification
        UriSpec<WebClient.RequestBodySpec> uriSpecPost1 = client1.method(HttpMethod.POST);
        UriSpec<WebClient.RequestBodySpec> uriSpecPost2 = client2.post();
        UriSpec<?> requestGet = client3.get();

        // uri specification
        RequestBodySpec bodySpecPost = uriSpecPost1.uri("http://localhost:" + port + "/resource");
        RequestBodySpec bodySpecPostMultipart = uriSpecPost2.uri(uriBuilder -> uriBuilder.pathSegment("resource-multipart")
            .build());
        RequestBodySpec bodySpecOverridenBaseUri = createDefaultPostRequest().uri(URI.create("/resource"));
        RequestBodySpec fooBodySpecPost = createDefaultPostRequest().uri(URI.create("/resource-foo"));

        // request body specifications
        String bodyValue = "bodyValue";
        RequestHeadersSpec<?> headerSpecPost1 = bodySpecPost.body(BodyInserters.fromPublisher(Mono.just(bodyValue), String.class));
        RequestHeadersSpec<?> headerSpecPost2 = createDefaultPostResourceRequest().body(BodyInserters.fromValue(bodyValue));
        RequestHeadersSpec<?> headerSpecFooPost = fooBodySpecPost.body(BodyInserters.fromValue(new Foo("fooName")));
        RequestHeadersSpec<?> headerSpecGet = requestGet.uri("/resource");

        // request body specifications - using inserters
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("key1", "multipartValue1");
        map.add("key2", "multipartValue2");

        BodyInserter<MultiValueMap<String, Object>, ClientHttpRequest> inserterMultipart = BodyInserters.fromMultipartData(map);
        BodyInserter<Object, ReactiveHttpOutputMessage> inserterObject = BodyInserters.fromValue(new Object());
        BodyInserter<String, ReactiveHttpOutputMessage> inserterString = BodyInserters.fromValue(bodyValue);

        RequestHeadersSpec<?> headerSpecInserterMultipart = bodySpecPostMultipart.body(inserterMultipart);
        RequestHeadersSpec<?> headerSpecInserterObject = createDefaultPostResourceRequest().body(inserterObject);
        RequestHeadersSpec<?> headerSpecInserterString = createDefaultPostResourceRequest().body(inserterString);

        // request header specification
        RequestHeadersSpec<?> headerSpecInserterStringWithHeaders = headerSpecInserterString.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
            .acceptCharset(StandardCharsets.UTF_8)
            .ifNoneMatch("*")
            .ifModifiedSince(ZonedDateTime.now());

        // request
        ResponseSpec responseSpecPostString = headerSpecInserterStringWithHeaders.retrieve();
        String responsePostString = responseSpecPostString.bodyToMono(String.class)
            .block();
        String responsePostMultipart = headerSpecInserterMultipart.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        String responsePostWithBody1 = headerSpecPost1.retrieve()
            .bodyToMono(String.class)
            .block();
        String responsePostWithBody3 = headerSpecPost2.retrieve()
            .bodyToMono(String.class)
            .block();
        String responsePostFoo = headerSpecFooPost.retrieve()
            .bodyToMono(String.class)
            .block();
        ParameterizedTypeReference<Map<String, String>> ref = new ParameterizedTypeReference<Map<String, String>>() {
        };
        Map<String, String> responseGet = headerSpecGet.retrieve()
            .bodyToMono(ref)
            .block();
        String responsePostWithNoBody = bodySpecPost.retrieve()
            .bodyToMono(String.class)
            .block();

        // response assertions
        assertThat(responsePostString).isEqualTo("processed-bodyValue");
        assertThat(responsePostMultipart).isEqualTo("processed-multipartValue1-multipartValue2");
        assertThat(responsePostWithBody1).isEqualTo("processed-");
        assertThat(responsePostWithBody3).isEqualTo("processed-");
        assertThat(responseGet).containsEntry("field", "value");
        assertThat(responsePostWithNoBody).isEqualTo("processed-");
        assertThat(responsePostFoo).isEqualTo("processed-fooName");

        assertThrows(WebClientRequestException.class, () -> {
            String responsePostObject = headerSpecInserterObject.exchangeToMono(response -> response.bodyToMono(String.class))
                .block();
        });
        assertThrows(WebClientRequestException.class, () -> {
            bodySpecOverridenBaseUri.retrieve()
                .bodyToMono(String.class)
                .block();
        });

    }

    @Test
    public void givenWebClientWithTimeoutConfigurations_whenRequestUsingWronglyConfiguredPublisher_thenObtainTimeout() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS)));

        WebClient timeoutClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        BodyInserter<Publisher<String>, ReactiveHttpOutputMessage> inserterCompleteSuscriber = BodyInserters.fromPublisher(Subscriber::onComplete, String.class);
        RequestHeadersSpec<?> headerSpecInserterCompleteSuscriber = timeoutClient.post()
            .uri("/resource")
            .body(inserterCompleteSuscriber);
        WebClientRequestException exception = assertThrows(WebClientRequestException.class, () -> {
            headerSpecInserterCompleteSuscriber.retrieve()
                .bodyToMono(String.class)
                .block();
        });
        assertThat(exception.getCause()).isInstanceOf(ConnectTimeoutException.class);
    }

    private RequestBodyUriSpec createDefaultPostRequest() {
        return WebClient.create("http://localhost:" + port)
            .post();
    }

    private RequestBodySpec createDefaultPostResourceRequest() {
        return createDefaultPostRequest().uri("/resource");
    }
}
