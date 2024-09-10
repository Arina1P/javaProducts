package com.example.products.config;

import com.example.products.model.Role;
import com.example.products.model.User;
import com.example.products.repository.RoleRepository;
import com.example.products.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getAttribute("login");
        Integer githubId = oAuth2User.getAttribute("id");

        // Проверяем, существует ли пользователь в базе данных
        User existingUser = userRepository.findByGithubId(githubId);

        if (existingUser == null) {
            // Если пользователь не существует, создаем нового
            User newUser = new User(name, githubId);

            // Назначаем роль client
            Role clientRole = roleRepository.findByName("ROLE_CLIENT");
            newUser.setRole(clientRole);

            userRepository.save(newUser);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
