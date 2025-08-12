package com.tuna.tuna_lib.ratelimiter;

public class TokenBucket {
    private static long lastRefillTime = System.currentTimeMillis();
    private static final int CAPACITY = 10;
    private static int refillRate = 2;
    private static double currentTokens = 0;

    public static boolean tryAcquire() {
        long elapsed = System.currentTimeMillis() - lastRefillTime;
        if (elapsed > 0) {
            double newTokens = elapsed * refillRate;
            currentTokens = Math.min(CAPACITY, newTokens);
        }

        if (currentTokens >= 1) {
            currentTokens -= 1;
            lastRefillTime = System.currentTimeMillis();
            return true;
        } else {
            lastRefillTime = System.currentTimeMillis();
            return false;
        }
    }
}
