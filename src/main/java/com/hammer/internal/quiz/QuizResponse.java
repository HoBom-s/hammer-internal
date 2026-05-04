package com.hammer.internal.quiz;

import java.time.OffsetDateTime;
import java.util.List;

public record QuizResponse(
        Long id,
        String question,
        List<String> choices,
        int correctIndex,
        String explanation,
        OffsetDateTime createdAt
) {

    static QuizResponse from(Quiz quiz) {
        return new QuizResponse(
                quiz.getId(),
                quiz.getQuestion(),
                List.of(quiz.getChoice1(), quiz.getChoice2(), quiz.getChoice3(), quiz.getChoice4()),
                quiz.getCorrectIndex(),
                quiz.getExplanation(),
                quiz.getCreatedAt());
    }
}
