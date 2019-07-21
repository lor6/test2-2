package com.baeldung.backoff.jitter;

import io.github.resilience4j.retry.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.baeldung.backoff.jitter.BackoffWithJitterTest.RetryProperties.*;
import static io.github.resilience4j.retry.IntervalFunction.ofExponentialBackoff;
import static io.github.resilience4j.retry.IntervalFunction.ofExponentialRandomBackoff;
import static java.util.Collections.nCopies;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BackoffWithJitterTest {

    static Logger log = LoggerFactory.getLogger(BackoffWithJitterTest.class);

    interface PingPongService {

        String call(String ping) throws PingPongServiceException;
    }

    class PingPongServiceException extends RuntimeException {

        public PingPongServiceException(String reason) {
            super(reason);
        }
    }

    private PingPongService service;
    private static final int NUM_TASKS = 8;

    @Before
    public void setUp() {
        service = mock(PingPongService.class);
    }

    @Test
    public void whenRetryExponentialBackoff_thenRetriedConfiguredNoOfTimes() {
        IntervalFunction intervalFn = ofExponentialBackoff(INITIAL_INTERVAL, MULTIPLIER);
        Function<String, String> pingPongFn = getRetryablePingPongFn(intervalFn);

        when(service.call(anyString())).thenThrow(PingPongServiceException.class);
        try {
            pingPongFn.apply("Hello");
        } catch (PingPongServiceException e) {
            verify(service, times(MAX_TRIES)).call(anyString());
        }
    }

    private Function<String, String> getRetryablePingPongFn(IntervalFunction intervalFn) {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(MAX_TRIES)
                .intervalFunction(intervalFn)
                .retryExceptions(PingPongServiceException.class)
                .build();
        Retry retry = Retry.of("pingpong", retryConfig);
        return Retry.decorateFunction(retry, ping -> {
            log.info("Invoked at {}", LocalDateTime.now());
            return service.call(ping);
        });
    }

    static class RetryProperties {
        static final Long INITIAL_INTERVAL = 1000L;
        static final Double MULTIPLIER = 2.0D;
        static final Double RANDOMIZATION_FACTOR = 0.6D;
        static final Integer MAX_TRIES = 4;
    }
}
