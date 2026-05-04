package com.hammer.internal.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameTest {

    @ParameterizedTest(name = "\"{0}\" is valid")
    @ValueSource(strings = {"nick", "a", "twelve_chars_ok!", "exactly20characters!"})
    void accepts_valid_nicknames(String name) {
        Nickname result = new Nickname(name);
        assertThat(result.value()).isEqualTo(name);
    }

    @Test
    void rejects_over_20_chars() {
        String tooLong = "a".repeat(21);
        assertThatThrownBy(() -> new Nickname(tooLong))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("20");
    }

    @Test
    void rejects_null() {
        assertThatThrownBy(() -> new Nickname(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void rejects_blank() {
        assertThatThrownBy(() -> new Nickname("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void toString_returns_raw_value() {
        assertThat(new Nickname("nick").toString()).isEqualTo("nick");
    }

    @Test
    void equality_based_on_value() {
        assertThat(new Nickname("same")).isEqualTo(new Nickname("same"));
    }
}
