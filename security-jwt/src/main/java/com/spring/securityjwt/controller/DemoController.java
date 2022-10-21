package com.spring.securityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class DemoController {
    @GetMapping("/demo")
    public String demo(Principal principal){
        return "Hello, "+principal.getName();
    }
}
