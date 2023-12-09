package com.wbrawner.recipes.repository;

import com.wbrawner.recipes.model.Session;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SessionRepository extends ReactiveCrudRepository<Session, String> {
}
