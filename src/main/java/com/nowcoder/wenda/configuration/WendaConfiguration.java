package com.nowcoder.wenda.configuration;


import com.nowcoder.wenda.interceptor.LoginRequiredInterceptor;
import com.nowcoder.wenda.interceptor.PassPortInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WendaConfiguration implements WebMvcConfigurer {

    @Autowired
    PassPortInterceptor passPortInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passPortInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
    }
}
