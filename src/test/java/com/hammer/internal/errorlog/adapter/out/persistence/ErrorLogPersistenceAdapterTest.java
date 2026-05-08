package com.hammer.internal.errorlog.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogSearchCriteria;
import com.hammer.internal.errorlog.domain.ErrorLog;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ErrorLogPersistenceAdapterTest {

    @Mock
    ErrorLogJpaRepository jpaRepository;

    @InjectMocks
    ErrorLogPersistenceAdapter sut;

    private static ErrorLogJpaEntity entity() {
        return new ErrorLogJpaEntity("GET", "/test", 500, "INTERNAL_ERROR", "msg", "stack", null);
    }

    @Test
    void save_delegates_to_repository() {
        sut.save("POST", "/api", 400, "BAD_REQUEST", "bad", "trace", "{}");

        then(jpaRepository).should().save(any(ErrorLogJpaEntity.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void search_with_empty_criteria_returns_all() {
        var criteria = new ErrorLogSearchCriteria(null, null, null, null, null);
        var page = new PageImpl<>(List.of(entity()), Pageable.ofSize(20), 1);
        given(jpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(page);

        PagedResult<ErrorLog> result = sut.search(criteria, 1, 20);

        assertThat(result.items()).hasSize(1);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void search_with_status_filter() {
        var criteria = new ErrorLogSearchCriteria(500, null, null, null, null);
        var page = new PageImpl<>(List.of(entity()), Pageable.ofSize(20), 1);
        given(jpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(page);

        PagedResult<ErrorLog> result = sut.search(criteria, 1, 20);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).getStatus()).isEqualTo(500);
    }

    @Test
    @SuppressWarnings("unchecked")
    void search_with_all_criteria() {
        var now = OffsetDateTime.now();
        var criteria = new ErrorLogSearchCriteria(500, "INTERNAL_ERROR", now.minusDays(1), now, "/test");
        var page = new PageImpl<>(List.of(entity()), Pageable.ofSize(10), 1);
        given(jpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(page);

        PagedResult<ErrorLog> result = sut.search(criteria, 1, 10);

        assertThat(result.items()).hasSize(1);
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    @SuppressWarnings("unchecked")
    void search_returns_empty_when_no_match() {
        var criteria = new ErrorLogSearchCriteria(999, null, null, null, null);
        var page = new PageImpl<ErrorLogJpaEntity>(List.of(), Pageable.ofSize(20), 0);
        given(jpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(page);

        PagedResult<ErrorLog> result = sut.search(criteria, 1, 20);

        assertThat(result.items()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }
}
