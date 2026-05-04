package com.hammer.internal.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserStatusTest {

    @Nested
    class FromCode {

        @ParameterizedTest(name = "code {0} → {1}")
        @CsvSource({"1, Active", "2, Suspended", "3, Deleted"})
        void maps_known_codes_to_status(short code, String expectedName) {
            UserStatus status = UserStatus.fromCode(code);

            assertThat(status.name()).isEqualTo(expectedName);
            assertThat(status.getCode()).isEqualTo(code);
        }

        @ParameterizedTest(name = "code {0} is unknown")
        @ValueSource(shorts = {0, 4, -1, 99})
        void throws_on_unknown_code(short unknownCode) {
            assertThatThrownBy(() -> UserStatus.fromCode(unknownCode))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(String.valueOf(unknownCode));
        }
    }

    @Test
    void roundtrip_code_conversion_is_consistent() {
        for (UserStatus status : UserStatus.values()) {
            assertThat(UserStatus.fromCode(status.getCode())).isEqualTo(status);
        }
    }
}
