package com.spring.jwtpractice.config;

import java.util.Locale;

public class Constants {

    public static final String LOGIN = "/login/**";
    public static final String USERS = "/users/**";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found";
    public static final String USER_FOUND_MESSAGE = "User found in the database: {}";
    public static final String LOGIN_WITH_USERNAME = "Login with username: {}";
    public static final String BAD_CREDENTIAL_MESSAGE = "Authentication failed for username: %s and password: %s";
    public static final String APPLICATION_JSON_VALUE = "application/json";

    public static final String AUTHENTICATION_ATTEMPT_ERROR_MESSAGE = "Error in attempt Authentication with username %s and password %s";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTH_PREFIX = "Bearer ";
}
