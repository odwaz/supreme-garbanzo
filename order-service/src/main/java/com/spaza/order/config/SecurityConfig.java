package com.spaza.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/**
 * Security Configuration for Order Service
 * 
 * IMPORTANT: This service sits behind API Gateway (port 8080)
 * - API Gateway validates JWT tokens before routing requests here
 * - This service should ONLY be accessible via API Gateway
 * - Direct access to this service (port 8087) should be blocked by firewall/network policy
 * 
 * In production:
 * - Use Kubernetes NetworkPolicy to restrict access to API Gateway only
 * - Or use service mesh (Istio) for mTLS between services
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .anyRequest().permitAll(); // Trust API Gateway - it validates JWT
        
        return http.build();
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
