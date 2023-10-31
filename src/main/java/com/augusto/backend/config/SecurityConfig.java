package com.augusto.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/products/**",
            "/categories/**"
    };

    private final ReactiveUserDetailsService userDetailService;

    @Autowired
    public SecurityConfig(ReactiveUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        http.authorizeExchange((exchanges) -> exchanges.pathMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll())
                .authorizeExchange().anyExchange().authenticated()
                .and().cors()
                .and().csrf().disable() // disables cors and csrf
                .authenticationManager(authenticationManager())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()); // set session to stateless

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return corsSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailService);
        authManager.setPasswordEncoder(bCryptPasswordEncoder());
        return authManager;
    }
}
