package com.hammer.internal.quiz.application.service;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.in.ListQuizzesUseCase;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class ListQuizzesService implements ListQuizzesUseCase {

    private final LoadQuizPort loadQuizPort;

    ListQuizzesService(LoadQuizPort loadQuizPort) {
        this.loadQuizPort = loadQuizPort;
    }

    @Override
    public PagedResult<QuizInfo> listQuizzes(int page, int size) {
        PagedResult<Quiz> result = loadQuizPort.findAll(page, size);
        return new PagedResult<>(
                result.items().stream().map(QuizInfo::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages());
    }
}
