package com.wbrawner.recipes.model;

public record UserResponse(String id, String username, String email) {
    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
