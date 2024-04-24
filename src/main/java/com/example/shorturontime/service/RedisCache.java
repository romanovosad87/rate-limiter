package com.example.shorturontime.service;

import com.example.shorturontime.token.TokenBucket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.convert.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisCache {

    private ValueOperations<String, TokenBucket> valueOperations;

    public RedisCache(final RedisTemplate<String, TokenBucket> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public TokenBucket getBucket(String key) {
        return valueOperations.get(key);
    }

    public void setValue(String key, TokenBucket bucket) {
        valueOperations.set(key, bucket, Duration.ofHours(12));
    }
}
