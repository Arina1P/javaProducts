package com.example.products.config;

import com.example.products.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                          CustomUserDetailsService customUserDetailsService) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/products").permitAll() // Доступен всем
                                .requestMatchers(HttpMethod.GET, "/products/{id}").hasAnyRole("CLIENT", "ADMIN") // Доступен клиентам и админам
                                .requestMatchers("/products/**").hasRole("ADMIN") // Остальные запросы /products/** доступны только админам
                                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureUrl("/login?error")
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }
}

// Для авторизации https://github.com/login/oauth/authorize?client_id=Ov23liANE8T6CVBYUwss&redirect_uri=http://localhost:8080/login/oauth2/code/github&scope=read:user user:email
// Получить код из ответа
