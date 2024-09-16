package com.example.products.config;

import com.example.products.model.Role;
import com.example.products.model.User;
import com.example.products.repository.RoleRepository;
import com.example.products.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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

        // Проверяем существующего пользователя
        User existingUser = userRepository.findByGithubId(githubId);

        if (existingUser == null) {
            // Создаем нового пользователя и назначаем роль
            User newUser = new User(name, githubId);
            Role clientRole = roleRepository.findByName("ROLE_CLIENT");
            newUser.setRole(clientRole);
            userRepository.save(newUser);
            existingUser = newUser;
        }

        // Устанавливаем Authentication с новой ролью
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                existingUser.getName(),
                null,  // Пароль не нужен
                List.of(new SimpleGrantedAuthority(existingUser.getRole().getName()))
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("http://localhost:5173/products");

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
