package com.example.products.config;

import com.example.products.model.User;
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

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println(111);
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getAttribute("login");
        Integer githubId = oAuth2User.getAttribute("id");

        // Проверяем, существует ли пользователь в базе данных
        User existingUser = userRepository.findByGithubId(githubId);

        if (existingUser == null) {
            // Если пользователь не существует, создаем нового
            User newUser = new User(name, githubId);
            userRepository.save(newUser);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
