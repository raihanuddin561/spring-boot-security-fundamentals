package com.spring.securityjwt.controller;

import com.spring.securityjwt.config.SecurityConfig;
import com.spring.securityjwt.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest({DemoController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
class DemoControllerTest {
    @Autowired
    MockMvc mvc;
    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void rootWhenAuthenticatedThenSayHelloUser() throws Exception {
        MvcResult result = this.mvc.perform(post("/token")
                .with(httpBasic("raihan","1234")))
                .andExpect(status().isOk())
                .andReturn();
        String token = result.getResponse().getContentAsString();
        this.mvc.perform(get("/demo")
                .header("Authorization","Bearer "+token))
                .andExpect(content().string("Hello, raihan"));

    }
    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/demo")).andExpect(status().isOk());
    }
}