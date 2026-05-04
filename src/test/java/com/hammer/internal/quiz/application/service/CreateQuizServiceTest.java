package com.hammer.internal.quiz.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hammer.internal.Fixtures;
import com.hammer.internal.quiz.application.dto.CreateQuizCommand;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.out.SaveQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateQuizServiceTest {

    @Mock
    SaveQuizPort saveQuizPort;

    @InjectMocks
    CreateQuizService sut;

    @Test
    void saves_new_quiz_and_returns_info() {
        var command = new CreateQuizCommand("Q?", "A", "B", "C", "D", 2, "설명");
        Quiz saved = Fixtures.quiz(10L);
        given(saveQuizPort.save(any(Quiz.class))).willReturn(saved);

        QuizInfo result = sut.create(command);

        assertThat(result.id()).isEqualTo(10L);
        then(saveQuizPort).should().save(any(Quiz.class));
    }
}
