package com.tuna.tuna_lib.httpguard.springboot;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.Map;

public class HttpGuardClientImpl implements HttpGuardClient {
    private final RestClient client;
    private final HttpGuardProperties.Retry retry;
    private final Map<String, String> defaultHeaders;

    public HttpGuardClientImpl(String baseUrl,
                               Duration connectTimeout, Duration readTimeout,
                               HttpGuardProperties.Retry retry,
                               Map<String, String> headers) {
        this.retry = retry;
        this.defaultHeaders = headers;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }


    @Override
    public <T> T get(String path, Class<T> type, Object... uriVars) {
        return execute(HttpMethod.GET, path, null, type, uriVars);
    }

    @Override
    public <T> T post(String path, Object body, Class<T> type, Object... uriVars) {
        return execute(HttpMethod.POST, path, body, type, uriVars);
    }

    @Override
    public <T> T put(String path, Object body, Class<T> type, Object... uriVars) {
        return execute(HttpMethod.PUT, path, body, type, uriVars);
    }

    @Override
    public void delete(String path, Object... uriVars) {
        execute(HttpMethod.DELETE, path, null, Void.class, uriVars);
    }

    private <T> T execute(HttpMethod httpMethod, String path, Object body, Class<T> type, Object... uriVars) {
        int attempts = 0;
        while (true) {
            attempts++;
            try {
                RestClient.RequestBodySpec spec = client.method(httpMethod)
                        .uri(path, uriVars)
                        .headers(h -> defaultHeaders.forEach(h::add));

                if (body != null) spec = spec.body(body);
                RestClient.ResponseSpec resp = spec.retrieve();

                return type == Void.class ? null : resp.body(type);
            } catch (RestClientResponseException ex) { // có status code
                int status = ex.getRawStatusCode();
                if (shouldRetryStatus(status) && attempts < retry.getMaxAttempts()) {
                    sleepBackoff(attempts); continue;
                }
                throw ex;

            } catch (RestClientException ex) {
                if (retry.isOnIOException() && attempts < retry.getMaxAttempts()) {
                    sleepBackoff(attempts); continue;
                }
                throw ex;
            }
        }
    }

    private boolean shouldRetryStatus(int s) {
        if (s == 429) return retry.isOn429();
        return s >= 500 && retry.isOn5xx();
    }

    private void sleepBackoff(int attempt) {
        long base = retry.getBackoff().toMillis();
        long max  = retry.getMaxBackoff().toMillis();
        long exp  = base * (1L << Math.max(0, attempt - 1)); // exponential
        long delay = Math.min(exp, max);
        double j = retry.getJitterPct();                     // jitter ±%
        double factor = 1.0 + (Math.random()*2*j - j);       // [1-j, 1+j]
        try { Thread.sleep((long)(delay * factor)); } catch (InterruptedException ignored) {}
    }
}
