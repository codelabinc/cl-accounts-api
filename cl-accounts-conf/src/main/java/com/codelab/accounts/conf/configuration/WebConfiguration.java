package com.codelab.accounts.conf.configuration;

import com.codelab.accounts.conf.interceptors.RequestPrincipalInterceptor;
import com.codelab.accounts.jwt.TokenService;
import com.codelab.accounts.service.principal.RequestPrincipal;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;

/**
 * @author lordUhuru 18/11/2019
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    private RequestPrincipalInterceptor requestPrincipalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestPrincipalInterceptor);
    }

    @Bean
    public FactoryBean<RequestPrincipal> requestPrincipal() {
        return RequestPrincipalInterceptor.requestPrincipal();
    }
}
