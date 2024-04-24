package com.example.shorturontime.filter;

import com.example.shorturontime.token.TokenBucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateInterceptor implements HandlerInterceptor {
    Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        System.out.println(remoteAddr);
        var tokenBucket = buckets.get(remoteAddr);
        if (tokenBucket != null) {
            if(!tokenBucket.consume()) {
              response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
              return false;
            } else {
                return true;
            }
        } else {
            tokenBucket = new TokenBucket();
            tokenBucket.consume();
            buckets.put(remoteAddr, tokenBucket);
            return true;
        }
    }
}
