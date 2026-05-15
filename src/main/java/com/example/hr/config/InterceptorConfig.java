package com.example.hr.config;

import com.example.hr.interceptor.AdminMutationAuditInterceptor;
import com.example.hr.interceptor.RequestTimingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình Web MVC interceptors.
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final RequestTimingInterceptor requestTimingInterceptor;
    private final AdminMutationAuditInterceptor adminMutationAuditInterceptor;

    public InterceptorConfig(RequestTimingInterceptor requestTimingInterceptor,
                             AdminMutationAuditInterceptor adminMutationAuditInterceptor) {
        this.requestTimingInterceptor = requestTimingInterceptor;
        this.adminMutationAuditInterceptor = adminMutationAuditInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/static/**");
        registry.addInterceptor(adminMutationAuditInterceptor)
                .addPathPatterns("/admin/**", "/manager/**", "/hiring/**", "/api/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/static/**");
    }
}
