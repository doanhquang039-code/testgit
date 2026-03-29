package com.example.hr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Dùng BCrypt để mã hóa mật khẩu, không bao giờ lưu mật khẩu dạng chữ thuần
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                   
                        .requestMatchers("/", "/index.html", "/login", "/login/**", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/hiring/**").hasAnyRole("ADMIN", "HIRING")
                        .requestMatchers("/videos/**").permitAll()
                      
                        .requestMatchers("/user1/**", "/user/**").authenticated()
                        .anyRequest().permitAll())
               .formLogin(login -> login
    .loginPage("/login")
    .passwordParameter("password") 
    .defaultSuccessUrl("/home", true)
    .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") 
                        .logoutSuccessUrl("/login?logout") 
                        .invalidateHttpSession(true) 
                        .clearAuthentication(true) 
                        .deleteCookies("JSESSIONID") 
                        .permitAll());

        return http.build();
    }
}
