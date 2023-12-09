package com.wbrawner.recipes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.wbrawner.recipes.Utils.randomString;

@Document
public class Session {
    @Id
    private String token;
    private String userId;
    private Instant expiration;

    public Session() {
        // Needed for Spring Data, shouldn't be called otherwise
    }

    public Session(String userId) {
        this.token = randomString(64);
        this.userId = userId;
        this.expiration = Instant.now().plus(14, ChronoUnit.DAYS);
    }

    public Session(String token, String userId, Instant expiration) {
        this.token = token;
        this.userId = userId;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getExpiration() {
        return expiration;
    }
}