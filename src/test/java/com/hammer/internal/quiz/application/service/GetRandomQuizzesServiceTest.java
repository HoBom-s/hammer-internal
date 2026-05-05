package com.hammer.internal.quiz.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRandomQuizzesServiceTest {

    @Mock
    LoadQuizPort loadQuizPort;

    @InjectMocks
    GetRandomQuizzesService sut;

    @Test
    void returns_random_quizzes() {
        List<Quiz> quizzes = List.of(Fixtures.quiz(1L), Fixtures.quiz(2L), Fixtures.quiz(3L));
        given(loadQuizPort.findRandom(3)).willReturn(quizzes);

        List<QuizInfo> result = sut.getRandomQuizzes(3);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(2).id()).isEqualTo(3L);
    }

    @Test
    void returns_empty_list_when_no_quizzes() {
        given(loadQuizPort.findRandom(3)).willReturn(List.of());

        List<QuizInfo> result = sut.getRandomQuizzes(3);

        assertThat(result).isEmpty();
    }
}
