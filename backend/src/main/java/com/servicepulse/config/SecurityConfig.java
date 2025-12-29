package com.servicepulse.config;

import com.servicepulse.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 🎓 CONFIGURATION SPRING SECURITY avec JWT
 * 
 * Configure l'authentification par token JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics
                .requestMatchers("/api/auth/**").permitAll()      // Login
                .requestMatchers("/api/services").permitAll()     // Liste services (lecture)
                .requestMatchers("/api/services/*/").permitAll()  // Détail service
                .requestMatchers("/api/services/status/**").permitAll()
                .requestMatchers("/api/services/alerts").permitAll()
                .requestMatchers("/api/services/search").permitAll()
                .requestMatchers("/api/services/health-summary").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/error").permitAll()
                
                // Tout le reste nécessite authentification
                .anyRequest().authenticated()
            )
            
            // Session stateless (pas de session côté serveur)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Ajouter le filtre JWT avant le filtre d'authentification standard
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
