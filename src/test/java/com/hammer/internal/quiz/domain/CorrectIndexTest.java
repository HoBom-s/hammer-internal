package com.hammer.internal.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CorrectIndexTest {

    @ParameterizedTest(name = "{0} is valid")
    @ValueSource(ints = {0, 1, 2, 3})
    void accepts_valid_range(int index) {
        CorrectIndex result = new CorrectIndex(index);
        assertThat(result.value()).isEqualTo(index);
    }

    @ParameterizedTest(name = "{0} is rejected")
    @ValueSource(ints = {-1, 4, 100, -100})
    void rejects_out_of_range(int index) {
        assertThatThrownBy(() -> new CorrectIndex(index))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("correctIndex");
    }
}
