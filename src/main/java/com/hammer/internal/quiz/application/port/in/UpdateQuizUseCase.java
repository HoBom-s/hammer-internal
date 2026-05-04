package com.hammer.internal.quiz.application.port.in;

import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.dto.UpdateQuizCommand;

public interface UpdateQuizUseCase {

    QuizInfo update(Long id, UpdateQuizCommand command);
}
