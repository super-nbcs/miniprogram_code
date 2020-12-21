package com.zfw.core.config;

import com.zfw.core.handler.AuthorizedHandler;
import com.zfw.core.handler.ParamValidateHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:zfw
 * @Date:2019/7/10
 * @Content:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${ops.api-version}")
    private String apiVersion;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String pattern = "/" + apiVersion + "/**";
        registry.addInterceptor(authorizedHandler()).addPathPatterns(pattern);
        registry.addInterceptor(paramValidateHandlerInterceptor()).addPathPatterns(pattern);
    }

    @Bean
    public AuthorizedHandler authorizedHandler(){
        return new AuthorizedHandler();
    }

    @Bean
    public ParamValidateHandlerInterceptor paramValidateHandlerInterceptor(){
        return new ParamValidateHandlerInterceptor();
    }

}
