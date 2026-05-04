package com.hammer.internal.quiz.application.port.in;

import com.hammer.internal.quiz.application.dto.CreateQuizCommand;
import com.hammer.internal.quiz.application.dto.QuizInfo;

public interface CreateQuizUseCase {

    QuizInfo create(CreateQuizCommand command);
}
