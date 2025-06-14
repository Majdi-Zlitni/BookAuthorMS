package tn.esprit.authorservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true) // To use @RolesAllowed, @Secured
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Use your application-specific roles here
                        .requestMatchers(HttpMethod.GET, "/api/authors/**").hasAnyAuthority("ROLE_APP_USER", "ROLE_APP_ADMIN") // Or just "APP_USER", "APP_ADMIN" if no "ROLE_" prefix is used/mapped
                        .requestMatchers(HttpMethod.POST, "/api/authors").hasAuthority("ROLE_APP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/authors/**").hasAuthority("ROLE_APP_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/authors/**").hasAuthority("ROLE_APP_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // Configure JWT validation (issuer-uri is picked from properties)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                );
        return http.build();
    }
}