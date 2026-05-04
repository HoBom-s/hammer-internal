package com.hammer.internal.notification.domain;

public record TemplateKey(String value) {

    public TemplateKey {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("templateKey must not be blank");
        }
        if (!value.matches("^[a-z][a-z0-9_]*$")) {
            throw new IllegalArgumentException("templateKey must be snake_case: " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
