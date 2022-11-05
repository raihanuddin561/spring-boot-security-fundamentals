package com.spring.jwtpractice.service;

import com.spring.jwtpractice.config.Constants;
import com.spring.jwtpractice.entity.RoleEntity;
import com.spring.jwtpractice.entity.UserEntity;
import com.spring.jwtpractice.repository.RoleRepository;
import com.spring.jwtpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserEntity user) {
        log.info("Saving user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserEntity addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        UserEntity userEntity = userRepository.findByUsername(username);
        RoleEntity roleEntity = roleRepository.findByName(roleName);
        userEntity.getRoles().add(roleEntity);
        return userEntity;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            String message = String.format(Constants.USER_NOT_FOUND_MESSAGE, user);
            log.error(message);
            throw new UsernameNotFoundException(message);
        } else {
            log.debug(Constants.USER_FOUND_MESSAGE, user);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new User(user.getUsername(), user.getPassword(), authorities);
        }
    }
}
