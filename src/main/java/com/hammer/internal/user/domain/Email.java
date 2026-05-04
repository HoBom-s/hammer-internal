package com.hammer.internal.user.domain;

public record Email(String value) {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (!value.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("invalid email format: " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
