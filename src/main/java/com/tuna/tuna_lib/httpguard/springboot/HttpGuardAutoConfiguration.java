package com.tuna.tuna_lib.httpguard.springboot;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(HttpGuardProperties.class)
public class HttpGuardAutoConfiguration {

    @Bean
    public HttpGuardFactory httpGuardFactory() {
        return new HttpGuardFactory();
    }

    @Bean
    public HttpGuardRegistry httpGuardRegistry(HttpGuardProperties props, HttpGuardFactory factory) {
        var defaults = props.getDefaults();
        Map<String, HttpGuardClient> map = new HashMap<>();

        props.getClients().forEach((name, c) -> {
            var connect = c.getConnectTimeout() != null ? c.getConnectTimeout() : defaults.getConnectTimeout();
            var read    = c.getReadTimeout()    != null ? c.getReadTimeout()    : defaults.getReadTimeout();
            var retry   = c.getRetry()          != null ? c.getRetry()          : defaults.getRetry();

            map.put(name, factory.create(
                    c.getBaseUrl(),
                    connect, read, retry,
                    c.getHeaders()
            ));
        });

        return new HttpGuardRegistry(map);
    }
}

