package com.hutu.shortlinkaccount.config;

import com.hutu.shortlinkcommon.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                //添加拦截的路径
                .addPathPatterns("/api/v1/account/**", "/api/v1/traffic/**")
                //排除不拦截
                .excludePathPatterns(
                        "/api/v1/account/register","/api/v1/account/upload","/api/v1/account/login",
                        "/api/v1/notify/captcha","/api/v1/notify/send_code");
    }
}
