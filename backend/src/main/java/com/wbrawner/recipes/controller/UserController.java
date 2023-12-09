package com.wbrawner.recipes.controller;

import com.wbrawner.recipes.model.*;
import com.wbrawner.recipes.repository.SessionRepository;
import com.wbrawner.recipes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ReactiveAuthenticationManager authenticationManager;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(ReactiveAuthenticationManager authenticationManager, SessionRepository sessionRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("me")
    public Mono<UserResponse> me() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (User) context.getAuthentication().getPrincipal())
                .map(UserResponse::of);
    }

    @PostMapping("login")
    public Mono<ResponseEntity<Session>> login(@RequestBody LoginRequest request) {
        return authenticationManager.authenticate(request.authentication())
                .doOnSuccess(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication))
                .doOnError(throwable -> logger.error("Login failed for " + request.username(), throwable))
                .flatMap(authentication -> {
                    var user = (User) authentication.getPrincipal();
                    var session = new Session(user.getId());
                    return sessionRepository.save(session);
                })
                .map(ResponseEntity::ok)
                .onErrorMap(t -> {
                    logger.error("Post-login session persistence failed for " + request.username(), t);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed", t);
                });
    }

    @PostMapping("register")
    public Mono<ResponseEntity<Session>> register(@RequestBody RegisterRequest request) {
        return userRepository.save(request.user(passwordEncoder))
                .doOnError(throwable -> logger.error("Registration failed for " + request.username(), throwable))
                .flatMap(user -> sessionRepository.save(new Session(user.getId())))
                .doOnError(throwable -> logger.error("Session creation failed for " + request.username(), throwable))
                .map(ResponseEntity::ok)
                .doOnError(throwable -> logger.error("Failed to map session to response entity for user " + request.username(), throwable))
                .onErrorMap(t -> {
                    logger.error("Registration failed", t);
                    var message = "Registration failed";
                    if (t instanceof DuplicateKeyException) {
                       message += ": username or email already taken";
                    }
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                })
                .doOnError(throwable -> logger.error("Other registration error for " + request.username(), throwable));
    }
}
