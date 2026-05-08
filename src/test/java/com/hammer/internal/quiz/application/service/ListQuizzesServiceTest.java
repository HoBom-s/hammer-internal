package com.hammer.internal.quiz.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.application.PagedResult;
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
class ListQuizzesServiceTest {

    @Mock
    LoadQuizPort loadQuizPort;

    @InjectMocks
    ListQuizzesService sut;

    @Test
    void returns_paged_quiz_info_without_keyword() {
        Quiz quiz = Fixtures.quiz();
        given(loadQuizPort.findAll(1, 20)).willReturn(new PagedResult<>(List.of(quiz), 1, 20, 1, 1));

        PagedResult<QuizInfo> result = sut.listQuizzes(1, 20, null);

        assertThat(result.items()).hasSize(1);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void returns_paged_quiz_info_with_keyword() {
        Quiz quiz = Fixtures.quiz();
        given(loadQuizPort.findByKeyword("2+2", 1, 20)).willReturn(new PagedResult<>(List.of(quiz), 1, 20, 1, 1));

        PagedResult<QuizInfo> result = sut.listQuizzes(1, 20, "2+2");

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).question()).isEqualTo("2+2는?");
    }

    @Test
    void blank_keyword_falls_back_to_find_all() {
        given(loadQuizPort.findAll(1, 20)).willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

        PagedResult<QuizInfo> result = sut.listQuizzes(1, 20, "   ");

        assertThat(result.items()).isEmpty();
    }

    @Test
    void returns_empty_page_when_no_quizzes() {
        given(loadQuizPort.findAll(1, 20)).willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

        PagedResult<QuizInfo> result = sut.listQuizzes(1, 20, null);

        assertThat(result.items()).isEmpty();
    }
}
