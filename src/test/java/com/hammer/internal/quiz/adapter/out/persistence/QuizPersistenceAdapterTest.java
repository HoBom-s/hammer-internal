package com.hammer.internal.quiz.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.domain.Quiz;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class QuizPersistenceAdapterTest {

    @Mock
    QuizJpaRepository jpaRepository;

    @InjectMocks
    QuizPersistenceAdapter sut;

    private static final OffsetDateTime FIXED = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    private static QuizJpaEntity entity(Long id) {
        return new QuizJpaEntity(id, "Q?", "A", "B", "C", "D", 0, "설명", FIXED);
    }

    @Test
    void findById_returns_domain() {
        given(jpaRepository.findById(1L)).willReturn(Optional.of(entity(1L)));

        Optional<Quiz> result = sut.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getQuestion()).isEqualTo("Q?");
    }

    @Test
    void findById_returns_empty() {
        given(jpaRepository.findById(99L)).willReturn(Optional.empty());

        assertThat(sut.findById(99L)).isEmpty();
    }

    @Test
    void findAll_returns_paged_result() {
        var page = new PageImpl<>(List.of(entity(1L), entity(2L)), Pageable.ofSize(20), 2);
        given(jpaRepository.findAll(any(Pageable.class))).willReturn(page);

        PagedResult<Quiz> result = sut.findAll(1, 20);

        assertThat(result.items()).hasSize(2);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.totalElements()).isEqualTo(2);
    }

    @Test
    void findByKeyword_returns_paged_result() {
        var page = new PageImpl<>(List.of(entity(1L)), Pageable.ofSize(20), 1);
        given(jpaRepository.findByQuestionContainingIgnoreCase(any(), any(Pageable.class)))
                .willReturn(page);

        PagedResult<Quiz> result = sut.findByKeyword("Q", 1, 20);

        assertThat(result.items()).hasSize(1);
    }

    @Test
    void existsById_delegates() {
        given(jpaRepository.existsById(1L)).willReturn(true);

        assertThat(sut.existsById(1L)).isTrue();
    }

    @Test
    void save_maps_and_persists() {
        var saved = entity(1L);
        given(jpaRepository.save(any(QuizJpaEntity.class))).willReturn(saved);

        Quiz quiz = new Quiz(null, "Q?", "A", "B", "C", "D", 0, "설명", FIXED);
        Quiz result = sut.save(quiz);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findRandom_returns_list() {
        given(jpaRepository.findRandom(3)).willReturn(List.of(entity(1L), entity(2L), entity(3L)));

        List<Quiz> result = sut.findRandom(3);

        assertThat(result).hasSize(3);
    }

    @Test
    void deleteById_delegates() {
        sut.deleteById(1L);

        then(jpaRepository).should().deleteById(1L);
    }
}
