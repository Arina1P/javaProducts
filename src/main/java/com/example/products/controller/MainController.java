package com.example.products.controller;

import com.example.products.model.Role;
import com.example.products.model.User;
import com.example.products.repository.RoleRepository;
import com.example.products.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;
// http://localhost:8080/admin/assignAdminRole/3

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String usersList(Model model, Principal principal) {
        model.addAttribute("authUser", principal);

        return "main";
    }
}

