package com.hammer.internal.quiz.application.service;

import com.hammer.internal.quiz.application.dto.CreateQuizCommand;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.in.CreateQuizUseCase;
import com.hammer.internal.quiz.application.port.out.SaveQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class CreateQuizService implements CreateQuizUseCase {

    private final SaveQuizPort saveQuizPort;

    CreateQuizService(SaveQuizPort saveQuizPort) {
        this.saveQuizPort = saveQuizPort;
    }

    @Override
    public QuizInfo create(CreateQuizCommand command) {
        Quiz quiz = new Quiz(
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
