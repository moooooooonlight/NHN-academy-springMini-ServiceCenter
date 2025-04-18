package com.nhnacademy.nhnmartservicecenter.config;

import com.nhnacademy.nhnmartservicecenter.interceptor.AuthCheckInterceptor;
import com.nhnacademy.nhnmartservicecenter.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthCheckInterceptor authCheckInterceptor;

    public WebConfig(AuthCheckInterceptor authCheckInterceptor) {
        this.authCheckInterceptor = authCheckInterceptor;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/custom/**")
                .addPathPatterns("/admin/**");
        registry.addInterceptor(authCheckInterceptor)
                .addPathPatterns("/custom/**")
                .addPathPatterns("/admin/**");
    }
}
