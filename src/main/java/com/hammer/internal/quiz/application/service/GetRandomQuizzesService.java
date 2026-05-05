package com.hammer.internal.quiz.application.service;

import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.in.GetRandomQuizzesUseCase;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetRandomQuizzesService implements GetRandomQuizzesUseCase {

    private final LoadQuizPort loadQuizPort;

    GetRandomQuizzesService(LoadQuizPort loadQuizPort) {
        this.loadQuizPort = loadQuizPort;
    }

    @Override
    public List<QuizInfo> getRandomQuizzes(int count) {
        return loadQuizPort.findRandom(count).stream().map(QuizInfo::from).toList();
    }
}
