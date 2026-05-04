package com.hammer.internal.quiz.application.dto;

import com.hammer.internal.quiz.domain.Quiz;
import java.time.OffsetDateTime;
import java.util.List;

public record QuizInfo(
        Long id,
        String question,
        List<String> choices,
        int correctIndex,
        String explanation,
        OffsetDateTime createdAt) {

    public static QuizInfo from(Quiz quiz) {
        return new QuizInfo(
                quiz.getId(),
                quiz.getQuestion(),
                List.of(quiz.getChoice1(), quiz.getChoice2(), quiz.getChoice3(), quiz.getChoice4()),
                quiz.getCorrectIndex(),
                quiz.getExplanation(),
                quiz.getCreatedAt());
    }
}
