package com.hammer.internal.quiz.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.dto.UpdateQuizCommand;
import com.hammer.internal.quiz.application.port.in.UpdateQuizUseCase;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import com.hammer.internal.quiz.application.port.out.SaveQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class UpdateQuizService implements UpdateQuizUseCase {

    private final LoadQuizPort loadQuizPort;
    private final SaveQuizPort saveQuizPort;

    UpdateQuizService(LoadQuizPort loadQuizPort, SaveQuizPort saveQuizPort) {
        this.loadQuizPort = loadQuizPort;
        this.saveQuizPort = saveQuizPort;
    }

    @Override
    public QuizInfo update(Long id, UpdateQuizCommand command) {
        Quiz quiz = loadQuizPort.findById(id).orElseThrow(() -> new NotFoundException("Quiz", id));
        quiz.update(
                command.question(),
                command.choice1(),
                command.choice2(),
                command.choice3(),
                command.choice4(),
                command.correctIndex(),
                command.explanation());
        Quiz saved = saveQuizPort.save(quiz);
        return QuizInfo.from(saved);
    }
}
