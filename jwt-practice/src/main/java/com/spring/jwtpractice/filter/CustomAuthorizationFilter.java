package com.spring.jwtpractice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwtpractice.config.Constants;
import com.spring.jwtpractice.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);
            if (authorizationHeader != null && authorizationHeader.startsWith(Constants.AUTH_PREFIX)) {
                try {
                    token = authorizationHeader.substring(Constants.AUTH_PREFIX.length());
                    UsernamePasswordAuthenticationToken authenticationToken = JwtUtil.parseToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error(String.format("Error auth token: $s", token), e);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    Map<String, String> error = new HashMap<>();
                    error.put("errorMessage", e.getMessage());
                    response.setContentType(Constants.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
