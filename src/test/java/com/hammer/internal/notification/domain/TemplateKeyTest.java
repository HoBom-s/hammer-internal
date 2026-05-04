package com.hammer.internal.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TemplateKeyTest {

    @ParameterizedTest(name = "\"{0}\" is valid")
    @ValueSource(strings = {"welcome_push", "order_confirm", "a", "reset_password_v2"})
    void accepts_valid_snake_case(String key) {
        TemplateKey result = new TemplateKey(key);
        assertThat(result.value()).isEqualTo(key);
    }

    @ParameterizedTest(name = "\"{0}\" is rejected")
    @ValueSource(strings = {"Welcome_push", "order-confirm", "123_invalid", "_leading", "has space"})
    void rejects_non_snake_case(String key) {
        assertThatThrownBy(() -> new TemplateKey(key))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("snake_case");
    }

    @Test
    void rejects_null() {
        assertThatThrownBy(() -> new TemplateKey(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void rejects_blank() {
        assertThatThrownBy(() -> new TemplateKey("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void toString_returns_raw_value() {
        assertThat(new TemplateKey("welcome_push").toString()).isEqualTo("welcome_push");
    }

    @Test
    void equality_based_on_value() {
        assertThat(new TemplateKey("same_key")).isEqualTo(new TemplateKey("same_key"));
    }
}
