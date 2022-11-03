package com.spring.jwtpractice.repository;

import com.spring.jwtpractice.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByName(String name);
}
