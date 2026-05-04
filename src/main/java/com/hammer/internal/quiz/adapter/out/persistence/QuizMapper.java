package com.hammer.internal.quiz.adapter.out.persistence;

import com.hammer.internal.quiz.domain.Quiz;

final class QuizMapper {

    private QuizMapper() {}

    static Quiz toDomain(QuizJpaEntity entity) {
        return new Quiz(
                entity.getId(),
                entity.getQuestion(),
                entity.getChoice1(),
                entity.getChoice2(),
                entity.getChoice3(),
                entity.getChoice4(),
                entity.getCorrectIndex(),
                entity.getExplanation(),
                entity.getCreatedAt());
    }

    static QuizJpaEntity toJpaEntity(Quiz quiz) {
        return new QuizJpaEntity(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getChoice1(),
                quiz.getChoice2(),
                quiz.getChoice3(),
                quiz.getChoice4(),
                quiz.getCorrectIndex(),
                quiz.getExplanation(),
                quiz.getCreatedAt());
    }
}
