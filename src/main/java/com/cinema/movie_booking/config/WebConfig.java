package com.cinema.movie_booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Mở cửa cho tất cả các đường dẫn bắt đầu bằng /api/
                .allowedOrigins("http://localhost:3000") // CHỈ cho phép Frontend của nhóm bạn gọi vào
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các hành động này
                .allowedHeaders("*") // Cho phép tất cả các loại Header (rất quan trọng để gửi Token JWT)
                .allowCredentials(true); // Cho phép gửi Cookie/Token xác thực
    }
}