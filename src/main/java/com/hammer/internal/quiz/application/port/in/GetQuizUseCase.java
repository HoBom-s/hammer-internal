package com.hammer.internal.quiz.application.port.in;

import com.hammer.internal.quiz.application.dto.QuizInfo;

public interface GetQuizUseCase {

    QuizInfo getQuiz(Long id);
}
