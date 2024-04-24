package com.example.shorturontime.token;

import java.io.Serializable;

public class TokenBucket implements Serializable {
    private static final int CAPACITY = 3;
    private static final long REFILL_TIME = 60_000;   // 1 minute
    private int tokens;
    private long lastRefillTime;

    public TokenBucket() {
        this.tokens = CAPACITY;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public boolean consume() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            System.out.println("The number of token is " + tokens);
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefillTime > REFILL_TIME) {
            tokens = CAPACITY;
            lastRefillTime = currentTime;
        }
    }

    @Override
    public String toString() {
        return "TokenBucket{"
                + "tokens=" + tokens
                + ", lastRefillTime=" + lastRefillTime
                + '}';
    }
}
