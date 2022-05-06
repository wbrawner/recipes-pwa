package com.wbrawner.recipes.config;

import com.wbrawner.recipes.model.TokenAuthentication;
import com.wbrawner.recipes.repository.SessionRepository;
import com.wbrawner.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, SessionRepository sessionRepository, UserRepository userRepository) {
        super(userDetailsService);
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof RememberMeAuthenticationToken) {
            return Mono.just(authentication);
        } else if (authentication instanceof TokenAuthentication) {
            final var token = (String) authentication.getCredentials();
            return sessionRepository.findById(token)
                    .flatMap(session -> userRepository.findById(session.getUserId()))
                    .flatMap(user -> Mono.just(new TokenAuthentication(token, user)));
        } else {
            return super.authenticate(authentication);
        }
    }
}
