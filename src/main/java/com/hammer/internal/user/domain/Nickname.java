package com.hammer.internal.user.domain;

public record Nickname(String value) {

    private static final int MAX_LENGTH = 20;

    public Nickname {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("nickname must not be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("nickname must not exceed " + MAX_LENGTH + " characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
