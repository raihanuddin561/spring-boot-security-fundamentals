package com.spring.securityjwt.controller;

import com.spring.securityjwt.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    private static final Logger log= LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    public AuthController(TokenService tokenService){
        this.tokenService=tokenService;
    }
    @PostMapping("/token")
    public String token(Authentication authentication){
        log.debug("Token requested for {}",authentication.getName());
        String token = tokenService.generateToken(authentication);
        log.debug("Token generated {}",token);
        return token;
    }
}
