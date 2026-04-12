

package com.example.hr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ URL /test1/** vào thư mục vật lý public/test1/
        registry.addResourceHandler("/test1/**")
                .addResourceLocations("file:public/test1/");
        // Also map /uploads/** to the same directory for profile images
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:public/test1/");
    }
}