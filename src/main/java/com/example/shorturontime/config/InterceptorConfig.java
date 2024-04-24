package com.example.shorturontime.config;

import com.example.shorturontime.interceptor.RateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final RateInterceptor rateInterceptor;

    public InterceptorConfig(RateInterceptor rateInterceptor) {
        this.rateInterceptor = rateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateInterceptor).addPathPatterns("/short");
    }
}
