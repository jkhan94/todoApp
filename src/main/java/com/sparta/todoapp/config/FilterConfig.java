package com.sparta.todoapp.config;

import com.sparta.todoapp.filter.AuthFilter;
import com.sparta.todoapp.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// spring util class static 검색
// effective java 책
@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final LoggingFilter loggingFilter;
    private final AuthFilter authFilter;

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterBean() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loggingFilter);
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*"); // 해당 URL은 필터 적용

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter);
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/*"); // 해당 URL은 필터 적용

        return registrationBean;
    }
}
