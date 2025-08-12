package com.tuna.tuna_lib.httpguard.springboot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties("http-guard")
public class HttpGuardProperties {
    private Defaults defaults = new Defaults();
    private Map<String, Client> clients = new HashMap<>();





    /* ---------------------------------------------------------*/
    @Getter
    @Setter
    public static class Defaults {
        private Duration connectTimeout = Duration.ofSeconds(1);
        private Duration readTimeout = Duration.ofSeconds(2);
        private Retry retry = new Retry();
    }

    @Getter
    @Setter
    public static class Client {
        private String baseUrl;
        private Duration connectTimeout;
        private Duration readTimeout;
        private Retry retry;
        private Map<String,String> headers = new HashMap<>();
    }

    @Getter
    @Setter
    public static class Retry {
        private int maxAttempts = 3;
        private Duration backoff = Duration.ofMillis(100);
        private Duration maxBackoff = Duration.ofSeconds(1);
        private double jitterPct = 0.2;
        private boolean on5xx = true, on429 = true, onIOException = true;
    }
}