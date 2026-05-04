package com.hammer.internal.quiz.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetQuizServiceTest {

    @Mock
    LoadQuizPort loadQuizPort;

    @InjectMocks
    GetQuizService sut;

    @Test
    void returns_quiz_info_when_found() {
        Quiz quiz = Fixtures.quiz(1L);
        given(loadQuizPort.findById(1L)).willReturn(Optional.of(quiz));

        QuizInfo result = sut.getQuiz(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.choices()).hasSize(4);
    }

    @Test
    void throws_not_found_when_absent() {
        given(loadQuizPort.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getQuiz(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("999");
    }
}
