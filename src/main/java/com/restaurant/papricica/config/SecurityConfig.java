package com.restaurant.papricica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // For a pure JSON API you usually disable CSRF (later you can tighten this with tokens)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Actuator health/info for monitoring
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        // Your public API endpoints (MVP)
                        .requestMatchers(HttpMethod.GET, "/api/v1/health").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/reservations").permitAll()

                        // Swagger/OpenAPI (if you add springdoc)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Admin endpoints later
                        .requestMatchers("/api/v1/admin/**").authenticated()

                        // Everything else: permit for now (or lock down if you prefer)
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

