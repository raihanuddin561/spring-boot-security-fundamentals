package com.spring.jwtpractice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwtpractice.config.Constants;
import com.spring.jwtpractice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> map = objectMapper.readValue(request.getInputStream(), Map.class);
            username = map.get("username");
            password = map.get("password");
            log.debug(Constants.LOGIN_WITH_USERNAME, username);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            log.error(String.format(Constants.BAD_CREDENTIAL_MESSAGE, username, password));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", e.getMessage());
            response.setContentType(Constants.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            throw new BadCredentialsException(Constants.BAD_CREDENTIAL_MESSAGE);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", e.getMessage());
            response.setContentType(Constants.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            throw new RuntimeException(String.format(Constants.AUTHENTICATION_ATTEMPT_ERROR_MESSAGE, username, password));
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String accessToken = JwtUtil.createAccessToken(user.getUsername(), request.getRequestURL().toString(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        String refreshToken = JwtUtil.createRefreshToken(user.getUsername());
        response.addHeader("access_token", accessToken);
        response.addHeader("refresh_token", refreshToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", "Bad Credentials");
        mapper.writeValue(response.getOutputStream(), error);
    }
}
