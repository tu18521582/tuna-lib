package com.tuna.tuna_lib.httpguard.springboot;

import java.time.Duration;
import java.util.Map;

public class HttpGuardFactory {
    public HttpGuardClient create(String baseUrl,
                                  Duration connect, Duration read,
                                  HttpGuardProperties.Retry retry,
                                  Map<String,String> headers) {
        return new HttpGuardClientImpl(baseUrl, connect, read, retry, headers);
    }
}
