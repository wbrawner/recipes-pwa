package com.wbrawner.recipes.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterRequest(String username, String password, String email) {
    public UsernamePasswordAuthenticationToken authentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    public User user(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }
}
