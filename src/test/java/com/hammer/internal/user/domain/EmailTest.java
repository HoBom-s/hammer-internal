package com.hammer.internal.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @ParameterizedTest(name = "\"{0}\" is valid")
    @ValueSource(strings = {"user@example.com", "test.name+tag@domain.co.kr", "a@b.io"})
    void accepts_valid_emails(String email) {
        Email result = new Email(email);
        assertThat(result.value()).isEqualTo(email);
    }

    @ParameterizedTest(name = "\"{0}\" is rejected")
    @ValueSource(strings = {"invalid", "@domain.com", "user@", "user@.com", "user@domain"})
    void rejects_invalid_formats(String email) {
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");
    }

    @Test
    void rejects_null() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void rejects_blank() {
        assertThatThrownBy(() -> new Email("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void toString_returns_raw_value() {
        assertThat(new Email("user@example.com").toString()).isEqualTo("user@example.com");
    }

    @Test
    void equality_based_on_value() {
        assertThat(new Email("a@b.com")).isEqualTo(new Email("a@b.com"));
    }
}
