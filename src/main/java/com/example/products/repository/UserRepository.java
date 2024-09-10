package com.example.products.repository;

import com.example.products.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByGithubId(Integer githubId); // Поиск по external id
    User findByName(String name); // Поиск по external id
}