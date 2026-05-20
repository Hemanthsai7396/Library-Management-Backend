package com.klef.Library.Management.Config;

import com.klef.Library.Management.Security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {})  // rely on CorsConfig bean
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public auth routes
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                // Public book listing (GET)
                .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**").permitAll()
                // H2 console for dev
                .requestMatchers("/h2-console/**").permitAll()
                // Health check
                .requestMatchers("/api/health").permitAll()
                // Admin-only mutations
                .requestMatchers(HttpMethod.POST,   "/api/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
                .requestMatchers("/api/requests/*/approve",
                                 "/api/requests/*/reject",
                                 "/api/requests/*/collected",
                                 "/api/requests/*/returned").hasRole("ADMIN")
                // Everything else needs authentication
                .anyRequest().authenticated()
            )
            .headers(h -> h.frameOptions(fo -> fo.disable())) // for H2 console
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
