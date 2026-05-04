package com.hammer.internal.quiz.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.dto.UpdateQuizCommand;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import com.hammer.internal.quiz.application.port.out.SaveQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateQuizServiceTest {

    @Mock
    LoadQuizPort loadQuizPort;

    @Mock
    SaveQuizPort saveQuizPort;

    @InjectMocks
    UpdateQuizService sut;

    @Test
    void loads_then_updates_then_saves() {
        Quiz existing = Fixtures.quiz(1L);
        given(loadQuizPort.findById(1L)).willReturn(Optional.of(existing));
        given(saveQuizPort.save(any(Quiz.class))).willAnswer(invocation -> invocation.getArgument(0));

        var command = new UpdateQuizCommand("수정된 질문", "A2", "B2", "C2", "D2", 3, "수정된 설명");
        QuizInfo result = sut.update(1L, command);

        assertThat(result.question()).isEqualTo("수정된 질문");
        assertThat(result.correctIndex()).isEqualTo(3);
    }

    @Test
    void throws_not_found_when_quiz_absent() {
        given(loadQuizPort.findById(999L)).willReturn(Optional.empty());

        var command = new UpdateQuizCommand("Q", "A", "B", "C", "D", 0, "E");

        assertThatThrownBy(() -> sut.update(999L, command)).isInstanceOf(NotFoundException.class);
        then(saveQuizPort).should(never()).save(any());
    }
}
