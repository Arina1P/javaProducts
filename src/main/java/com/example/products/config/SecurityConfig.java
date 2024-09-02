package com.example.products.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// для авторизации https://github.com/login/oauth/authorize?client_id=Ov23liANE8T6CVBYUwss&redirect_uri=http://localhost:8080/login/oauth2/code/github&scope=read:user user:email
// получить код из ответа

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Разрешаем доступ к /products для всех, а остальное требует аутентификации
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/products").permitAll() // Доступ к /products разрешен для всех
                                .requestMatchers("/products/**").authenticated() // Доступ к /products/** требует аутентификации
                                .anyRequest().permitAll() // Все остальные запросы разрешены
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/products", true) // Перенаправление после успешного входа
                                .failureUrl("/login?error") // Перенаправление при ошибке входа
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login?logout") // Перенаправление после успешного выхода
                                .permitAll() // Доступ к logout для всех
                );
        return http.build();
    }
}
