package com.wbrawner.recipes.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class TokenAuthentication extends AbstractAuthenticationToken {
    private final String token;
    private final User user;

    public TokenAuthentication(String token) {
        this(token, null);
    }

    public TokenAuthentication(String token, User user) {
        super(List.of(new SimpleGrantedAuthority("USER")));
        this.token = token;
        this.user = user;
        setAuthenticated(user != null);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
