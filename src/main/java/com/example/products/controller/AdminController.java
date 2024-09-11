package com.example.products.controller;

import com.example.products.model.Role;
import com.example.products.model.User;
import com.example.products.repository.RoleRepository;
import com.example.products.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
// http://localhost:8080/admin/assignAdminRole/3

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/assignAdminRole/{userId}")
    public ResponseEntity<String> assignAdminRole(@PathVariable Long userId) {
        // Находим пользователя по ID
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        // Находим роль ADMIN
        Optional<Role> adminRole = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"));

        // Назначаем найденную роль пользователю
        user.setRole(adminRole.get());

        // Сохраняем изменения в БД
        userRepository.save(user);

        return ResponseEntity.ok("Role ADMIN assigned to user with ID: " + userId);
    }
}

