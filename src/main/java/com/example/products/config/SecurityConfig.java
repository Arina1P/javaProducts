package com.example.products.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/products").permitAll()
                                .requestMatchers("/products/**").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .successHandler(oAuth2LoginSuccessHandler) // Подключаем обработчик успешного входа
                                .failureUrl("/login?error")
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );
        return http.build();
    }
}

// Для авторизации https://github.com/login/oauth/authorize?client_id=Ov23liANE8T6CVBYUwss&redirect_uri=http://localhost:8080/login/oauth2/code/github&scope=read:user user:email
// Получить код из ответа
