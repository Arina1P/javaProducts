package com.example.products.controller;

import com.example.products.model.Role;
import com.example.products.model.User;
import com.example.products.repository.RoleRepository;
import com.example.products.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
// http://localhost:8080/admin/assignAdminRole/3

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String usersList(Model model) {
        model.addAttribute("users", userRepository.findAll());

        return "users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);

        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/assignAdminRole/{userId}")
    public String assignAdminRole(@PathVariable Long userId) {
        // Находим пользователя по ID
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }

        User user = optionalUser.get();

        // Находим роль ADMIN
        Optional<Role> adminRole = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"));

        // Назначаем найденную роль пользователю
        user.setRole(adminRole.get());

        // Сохраняем изменения в БД
        userRepository.save(user);

        return "redirect:/admin/users";
    }
}

