package com.lucas.crypto_api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtFilter jwtFilterBean() {
        return new JwtFilter(); // Cria o filtro manualmente
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/usuarios/*", "/carteiras/*", "/criptomoedas/*");
        return registration;
    }
}
