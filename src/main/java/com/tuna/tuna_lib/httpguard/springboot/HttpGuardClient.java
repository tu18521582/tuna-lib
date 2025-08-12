package com.tuna.tuna_lib.httpguard.springboot;

public interface HttpGuardClient {
    <T> T get(String path, Class<T> type, Object... uriVars);
    <T> T post(String path, Object body, Class<T> type, Object... uriVars);
    <T> T put(String path, Object body, Class<T> type, Object... uriVars);
    void delete(String path, Object... uriVars);
}
