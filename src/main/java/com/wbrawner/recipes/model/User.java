package com.wbrawner.recipes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static com.wbrawner.recipes.Utils.randomString;

@Document
public class User implements UserDetails {
    @Id
    private String id;
    @Indexed(unique=true)
    private String username;
    private String password;
    @Indexed(unique=true)
    private String email;
    private Set<GrantedAuthority> authorities;
    private boolean accountExpired = false;
    private boolean credentialsExpired = false;
    private boolean enabled = false;
    private boolean locked = false;

    public User() {
        // Needed for mongo
    }

    public User(
            String username,
            String password,
            String email
    ) {
        this(
                randomString(32),
                username,
                password,
                email,
                Set.of(new SimpleGrantedAuthority("USER")),
                false,
                false,
                true,
                false
        );
    }

    public User(
            String id,
            String username,
            String password,
            String email,
            Set<GrantedAuthority> authorities,
            boolean accountExpired,
            boolean credentialsExpired,
            boolean enabled,
            boolean locked
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.accountExpired = accountExpired;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }
}
