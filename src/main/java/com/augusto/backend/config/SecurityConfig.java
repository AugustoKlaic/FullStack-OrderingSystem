package com.augusto.backend.config;

import com.augusto.backend.security.CustomAuthenticationManager;
import com.augusto.backend.security.CustomAuthorizationManager;
import com.augusto.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/products/**",
            "/categories/**",
            "/address/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/login/**",
            "/clients",
            "/forgot-password/**"
    };

    private final CustomAuthorizationManager customAuthorizationManager;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(CustomAuthorizationManager customAuthorizationManager,
                          CustomAuthenticationManager customAuthenticationManager,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customAuthorizationManager = customAuthorizationManager;
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange((exchanges) -> exchanges.pathMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                        .pathMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                        .anyExchange().access(customAuthorizationManager))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()); // set session to stateless
        return http.build();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(customAuthenticationManager);
        filter.setServerAuthenticationConverter(jwtAuthenticationFilter);
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // permit all origins
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        configuration.addExposedHeader(HttpHeaders.AUTHORIZATION);
        configuration.addExposedHeader(HttpHeaders.LOCATION);

        final UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", configuration);
        return corsSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
