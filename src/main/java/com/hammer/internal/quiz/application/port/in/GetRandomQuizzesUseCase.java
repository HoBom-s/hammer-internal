package com.hammer.internal.quiz.application.port.in;

import com.hammer.internal.quiz.application.dto.QuizInfo;
import java.util.List;

public interface GetRandomQuizzesUseCase {

    List<QuizInfo> getRandomQuizzes(int count);
}
