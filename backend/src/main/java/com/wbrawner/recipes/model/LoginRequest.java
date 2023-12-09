package com.wbrawner.recipes.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public record LoginRequest(String username, String password) {
    public UsernamePasswordAuthenticationToken authentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
