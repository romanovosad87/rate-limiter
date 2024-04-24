package com.example.shorturontime.interceptor;

import com.example.shorturontime.service.RedisCache;
import com.example.shorturontime.token.TokenBucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.convert.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateInterceptor implements HandlerInterceptor {
//    Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private RedisCache cache;

    public RateInterceptor(RedisCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        System.out.println(remoteAddr);
        var tokenBucket = cache.getBucket(remoteAddr);
        if (tokenBucket != null) {
            if(!tokenBucket.consume()) {
              response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
              return false;
            } else {
                cache.setValue(remoteAddr, tokenBucket);
                return true;
            }
        } else {
            tokenBucket = new TokenBucket();
            tokenBucket.consume();
            cache.setValue(remoteAddr, tokenBucket);
            return true;
        }
    }
}
