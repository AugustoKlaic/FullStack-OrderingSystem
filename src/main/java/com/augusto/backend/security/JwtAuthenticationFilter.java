package com.augusto.backend.security;

import com.augusto.backend.dto.CredentialsDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpRequest,
                                                HttpServletResponse httpResponse) throws AuthenticationException {

        try {
            CredentialsDto credentials = mapper.readValue(httpRequest.getInputStream(), CredentialsDto.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),
                                                                                                    credentials.getPassword(),
                                                                                                    List.of());
            return this.authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void successfulAuthentication(HttpServletRequest httpRequest,
                                      HttpServletResponse httpResponse,
                                      FilterChain filterChain,
                                      Authentication auth) throws IOException, ServletException {

        String username = ((UserSpringSecurity) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        httpResponse.addHeader("Authorization", "Bearer" + token);
    }
}
