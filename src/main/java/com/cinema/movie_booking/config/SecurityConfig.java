package com.cinema.movie_booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF để có thể gọi POST/PUT từ Postman
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Cho phép TẤT CẢ các request mà không cần đăng nhập
                )
                .formLogin(form -> form.disable()) // Tắt cái bảng đăng nhập hiện ra trên trình duyệt
                .httpBasic(basic -> basic.disable()); // Tắt hộp thoại đăng nhập cơ bản

        return http.build();
    }

}
