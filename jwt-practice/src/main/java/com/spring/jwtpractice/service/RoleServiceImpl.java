package com.spring.jwtpractice.service;

import com.spring.jwtpractice.entity.RoleEntity;
import com.spring.jwtpractice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService{
    @Autowired
    private final RoleRepository roleRepository;

    @Override
    public void save(RoleEntity role_user) {
        roleRepository.save(role_user);
    }
}
