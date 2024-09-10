package com.example.products.config;

import com.example.products.model.Role;
import com.example.products.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository) {
        return args -> {
            // Проверяем, есть ли роль CLIENT, если нет — создаем
            if (roleRepository.findByName("ROLE_CLIENT") == null) {
                roleRepository.save(new Role("ROLE_CLIENT"));
            }
            // Проверяем, есть ли роль ADMIN, если нет — создаем
            if (roleRepository.findByName("ROLE_ADMIN") == null) {
                roleRepository.save(new Role("ROLE_ADMIN"));
            }
        };
    }
}
