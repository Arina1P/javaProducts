package com.example.products.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/url")
    public Map<String, String> getAuthUrl(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        HttpSession session = request.getSession();
        String state = request.getParameter("state");
        result.put("url", "https://github.com/login/oauth/authorize?response_type=code&client_id=Ov23lilkXq3azRQsyR7d&scope=read:user%20user:email&redirect_uri=http://localhost:8080/login/oauth2/code/github&state=" + state);

        return result;
    }
}
