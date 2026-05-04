package com.hammer.internal.quiz.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.quiz.application.port.in.DeleteQuizUseCase;
import com.hammer.internal.quiz.application.port.out.DeleteQuizPort;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class DeleteQuizService implements DeleteQuizUseCase {

    private final LoadQuizPort loadQuizPort;
    private final DeleteQuizPort deleteQuizPort;

    DeleteQuizService(LoadQuizPort loadQuizPort, DeleteQuizPort deleteQuizPort) {
        this.loadQuizPort = loadQuizPort;
        this.deleteQuizPort = deleteQuizPort;
    }

    @Override
    public void delete(Long id) {
        if (!loadQuizPort.existsById(id)) {
            throw new NotFoundException("Quiz", id);
        }
        deleteQuizPort.deleteById(id);
    }
}
