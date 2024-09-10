package com.example.products.service;

import com.example.products.model.User;
import com.example.products.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByName(username);

        System.out.println(user);

        // Преобразование User в UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getName(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()))
        );
    }
}
