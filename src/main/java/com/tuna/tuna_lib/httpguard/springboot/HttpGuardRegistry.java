package com.tuna.tuna_lib.httpguard.springboot;

import java.util.Collections;
import java.util.Map;

public class HttpGuardRegistry {
    private final Map<String, HttpGuardClient> clients;

    public HttpGuardRegistry(Map<String, HttpGuardClient> clients) {
        this.clients = Map.copyOf(clients);
    }
    public HttpGuardClient get(String name) {
        var c = clients.get(name);
        if (c == null) throw new IllegalArgumentException("No HttpGuard client: " + name);
        return c;
    }
    public Map<String, HttpGuardClient> asMap() {
        return Collections.unmodifiableMap(clients);
    }
}
