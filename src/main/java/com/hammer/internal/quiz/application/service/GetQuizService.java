package com.hammer.internal.quiz.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.in.GetQuizUseCase;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetQuizService implements GetQuizUseCase {

    private final LoadQuizPort loadQuizPort;

    GetQuizService(LoadQuizPort loadQuizPort) {
        this.loadQuizPort = loadQuizPort;
    }

    @Override
    public QuizInfo getQuiz(Long id) {
        return loadQuizPort.findById(id).map(QuizInfo::from).orElseThrow(() -> new NotFoundException("Quiz", id));
    }
}
