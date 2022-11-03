package com.spring.jwtpractice.service;

import com.spring.jwtpractice.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity save(UserEntity user);
    UserEntity addRoleToUser(String username,String roleName);

    List<UserEntity> findAll();

    UserEntity findByUsername(String username);
}
