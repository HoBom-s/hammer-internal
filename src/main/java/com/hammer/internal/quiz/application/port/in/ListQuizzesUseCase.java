package com.hammer.internal.quiz.application.port.in;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.application.dto.QuizInfo;

public interface ListQuizzesUseCase {

    PagedResult<QuizInfo> listQuizzes(int page, int size, String keyword);
}
