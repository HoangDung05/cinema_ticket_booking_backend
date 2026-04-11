package com.cinema.movie_booking.config;

import com.cinema.movie_booking.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // Inject JwtAuthFilter để đăng ký vào filter chain
    private final JwtAuthFilter jwtAuthFilter;

    // Công cụ băm mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Quản lý xác thực
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(org.springframework.security.config.Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()          // CORS preflight
                        .requestMatchers("/v3/api-docs/**").permitAll()                  // Swagger docs
                        .requestMatchers("/swagger-ui/**").permitAll()                   // Swagger UI
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()     
                        // .requestMatchers("/admin/**").permitAll()  // mới sửa để test cho admin                  // Đăng ký / Đăng nhập
                        .requestMatchers(HttpMethod.GET, "/movies/**").permitAll()       // Xem danh sách phim (public)
                        .requestMatchers(HttpMethod.GET, "/showtimes/**").permitAll()    // Xem lịch chiếu (public)
                        .requestMatchers("/bookings/**").permitAll()                     // Đặt vé / Tính giá (TODO: yêu cầu JWT sau)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                // Thêm JwtAuthFilter chạy TRƯỚC filter xác thực mặc định của Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
