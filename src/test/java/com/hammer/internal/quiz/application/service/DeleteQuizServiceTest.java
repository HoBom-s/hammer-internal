package com.hammer.internal.quiz.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.quiz.application.port.out.DeleteQuizPort;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteQuizServiceTest {

    @Mock
    LoadQuizPort loadQuizPort;

    @Mock
    DeleteQuizPort deleteQuizPort;

    @InjectMocks
    DeleteQuizService sut;

    @Test
    void deletes_when_exists() {
        given(loadQuizPort.existsById(1L)).willReturn(true);

        sut.delete(1L);

        then(deleteQuizPort).should().deleteById(1L);
    }

    @Test
    void throws_not_found_when_absent() {
        given(loadQuizPort.existsById(999L)).willReturn(false);

        assertThatThrownBy(() -> sut.delete(999L)).isInstanceOf(NotFoundException.class);
        then(deleteQuizPort).should(never()).deleteById(any());
    }
}
