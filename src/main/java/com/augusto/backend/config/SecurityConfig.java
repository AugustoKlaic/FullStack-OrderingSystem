package com.augusto.backend.config;

import com.augusto.backend.security.JwtUtil;
import com.augusto.backend.security.JwtWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/products/**",
            "/categories/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/login/**",
            "/clients/**",
            "/forgot-password/**"
    };

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, ReactiveAuthorizationManager<AuthorizationContext> authorizationManager) {
        http.authorizeExchange((exchanges) -> exchanges.pathMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                                                       .pathMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                                                       .anyExchange().access(authorizationManager))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .addFilterAt(jwtWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()); // set session to stateless
        return http.build();
    }

    @Bean
    public WebFilter jwtWebFilter(){
        return new JwtWebFilter(jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return corsSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
