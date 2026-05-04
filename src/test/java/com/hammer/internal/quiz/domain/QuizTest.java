package com.hammer.internal.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuizTest {

    @Nested
    class Creation {

        @Test
        void new_quiz_has_null_id_and_auto_generated_timestamp() {
            Quiz quiz = new Quiz("질문?", "A", "B", "C", "D", 0, "설명");

            assertThat(quiz.getId()).isNull();
            assertThat(quiz.getCreatedAt()).isNotNull();
            assertThat(quiz.getQuestion()).isEqualTo("질문?");
            assertThat(quiz.getCorrectIndex()).isZero();
        }

        @Test
        void reconstruction_preserves_id_and_timestamp() {
            OffsetDateTime fixedTime = OffsetDateTime.parse("2024-01-01T00:00:00+09:00");

            Quiz quiz = new Quiz(42L, "Q", "A", "B", "C", "D", 3, "E", fixedTime);

            assertThat(quiz.getId()).isEqualTo(42L);
            assertThat(quiz.getCreatedAt()).isEqualTo(fixedTime);
        }
    }

    @Nested
    class Update {

        @Test
        void replaces_all_mutable_fields() {
            Quiz quiz = new Quiz(1L, "Old Q", "A1", "B1", "C1", "D1", 0, "Old E", OffsetDateTime.now());

            quiz.update("New Q", "A2", "B2", "C2", "D2", 3, "New E");

            assertThat(quiz.getQuestion()).isEqualTo("New Q");
            assertThat(quiz.getChoice1()).isEqualTo("A2");
            assertThat(quiz.getChoice2()).isEqualTo("B2");
            assertThat(quiz.getChoice3()).isEqualTo("C2");
            assertThat(quiz.getChoice4()).isEqualTo("D2");
            assertThat(quiz.getCorrectIndex()).isEqualTo(3);
            assertThat(quiz.getExplanation()).isEqualTo("New E");
        }

        @Test
        void does_not_affect_id_or_createdAt() {
            OffsetDateTime fixedTime = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");
            Quiz quiz = new Quiz(7L, "Q", "A", "B", "C", "D", 0, "E", fixedTime);

            quiz.update("Updated", "A", "B", "C", "D", 1, "Updated E");

            assertThat(quiz.getId()).isEqualTo(7L);
            assertThat(quiz.getCreatedAt()).isEqualTo(fixedTime);
        }
    }
}
