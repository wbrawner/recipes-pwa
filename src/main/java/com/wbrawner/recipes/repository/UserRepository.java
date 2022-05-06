package com.wbrawner.recipes.repository;

import com.wbrawner.recipes.model.Recipe;
import com.wbrawner.recipes.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsernameIgnoreCase(String username);
    Mono<User> findByEmailIgnoreCase(String email);
}
