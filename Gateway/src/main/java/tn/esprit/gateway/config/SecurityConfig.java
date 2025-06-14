package tn.esprit.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Use WebFlux security for Spring Cloud Gateway
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // Example: Allow public access to actuator health on gateway
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Example: Secure routes to author-service, requiring authentication
                        .pathMatchers("/api/authors/**").authenticated() // Or specific roles: .hasRole("USER")
                        // Example: Secure routes to book-service
                        .pathMatchers("/books/**").authenticated()
                        // If gateway handles login/logout, those paths might be permitAll or handled differently
                        // .pathMatchers("/login", "/logout").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // JWT validation based on issuer-uri from properties
                );
        // If gateway is also an OAuth2 client for login:
        // .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}