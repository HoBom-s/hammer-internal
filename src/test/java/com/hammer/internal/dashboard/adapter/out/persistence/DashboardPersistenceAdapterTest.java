package com.hammer.internal.dashboard.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.hammer.internal.dashboard.application.dto.DashboardStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardPersistenceAdapterTest {

    @Mock
    EntityManager em;

    @InjectMocks
    DashboardPersistenceAdapter sut;

    @SuppressWarnings("unchecked")
    private TypedQuery<Long> mockCountQuery(long count) {
        TypedQuery<Long> q = mock(TypedQuery.class);
        given(q.getSingleResult()).willReturn(count);
        return q;
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<Long> mockCountQueryWithParam(long count) {
        TypedQuery<Long> q = mock(TypedQuery.class);
        given(q.setParameter(anyString(), any())).willReturn(q);
        given(q.getSingleResult()).willReturn(count);
        return q;
    }

    @Test
    void countUsers() {
        TypedQuery<Long> q = mockCountQuery(100L);
        given(em.createQuery(anyString(), eq(Long.class))).willReturn(q);

        assertThat(sut.countUsers()).isEqualTo(100L);
    }

    @Test
    void countUsersByStatus() {
        TypedQuery<Long> q = mockCountQueryWithParam(80L);
        given(em.createQuery(anyString(), eq(Long.class))).willReturn(q);

        assertThat(sut.countUsersByStatus((short) 1)).isEqualTo(80L);
    }

    @Test
    void countQuizzes() {
        TypedQuery<Long> q = mockCountQuery(50L);
        given(em.createQuery(anyString(), eq(Long.class))).willReturn(q);

        assertThat(sut.countQuizzes()).isEqualTo(50L);
    }

    @Test
    void countErrorLogsAfter() {
        TypedQuery<Long> q = mockCountQueryWithParam(10L);
        given(em.createQuery(anyString(), eq(Long.class))).willReturn(q);

        assertThat(sut.countErrorLogsAfter(OffsetDateTime.now())).isEqualTo(10L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void findTopErrorCodes() {
        TypedQuery<Tuple> q = mock(TypedQuery.class);
        Tuple tuple = mock(Tuple.class);
        given(tuple.get("errorCode", String.class)).willReturn("NOT_FOUND");
        given(tuple.get("cnt", Long.class)).willReturn(5L);
        given(q.setParameter(anyString(), any())).willReturn(q);
        given(q.setMaxResults(anyInt())).willReturn(q);
        given(q.getResultList()).willReturn(List.of(tuple));
        given(em.createQuery(anyString(), eq(Tuple.class))).willReturn(q);

        List<DashboardStats.ErrorCountByCode> result = sut.findTopErrorCodes(OffsetDateTime.now(), 5);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).errorCode()).isEqualTo("NOT_FOUND");
        assertThat(result.get(0).count()).isEqualTo(5L);
    }

    @Test
    void findDailyErrorCounts_with_sql_date() {
        Query q = mock(Query.class);
        Tuple tuple = mock(Tuple.class);
        given(tuple.get("log_date")).willReturn(Date.valueOf(LocalDate.of(2024, 6, 15)));
        given(tuple.get("cnt")).willReturn(10L);
        given(q.setParameter(anyString(), any())).willReturn(q);
        given(q.getResultList()).willReturn(List.of(tuple));
        given(em.createNativeQuery(anyString(), eq(Tuple.class))).willReturn(q);

        List<DashboardStats.DailyErrorCount> result = sut.findDailyErrorCounts(OffsetDateTime.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(result.get(0).count()).isEqualTo(10L);
    }

    @Test
    void findDailyErrorCounts_with_bigdecimal_count() {
        Query q = mock(Query.class);
        Tuple tuple = mock(Tuple.class);
        given(tuple.get("log_date")).willReturn(Date.valueOf(LocalDate.of(2024, 1, 1)));
        given(tuple.get("cnt")).willReturn(new java.math.BigDecimal(42));
        given(q.setParameter(anyString(), any())).willReturn(q);
        given(q.getResultList()).willReturn(List.of(tuple));
        given(em.createNativeQuery(anyString(), eq(Tuple.class))).willReturn(q);

        List<DashboardStats.DailyErrorCount> result = sut.findDailyErrorCounts(OffsetDateTime.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).count()).isEqualTo(42L);
    }

    @Test
    void findDailyErrorCounts_with_string_date() {
        Query q = mock(Query.class);
        Tuple tuple = mock(Tuple.class);
        given(tuple.get("log_date")).willReturn("2024-03-20");
        given(tuple.get("cnt")).willReturn(7L);
        given(q.setParameter(anyString(), any())).willReturn(q);
        given(q.getResultList()).willReturn(List.of(tuple));
        given(em.createNativeQuery(anyString(), eq(Tuple.class))).willReturn(q);

        List<DashboardStats.DailyErrorCount> result = sut.findDailyErrorCounts(OffsetDateTime.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).date()).isEqualTo(LocalDate.of(2024, 3, 20));
    }

    @Test
    void findTopErrorCodes_empty() {
        @SuppressWarnings("unchecked")
        TypedQuery<Tuple> q = mock(TypedQuery.class);
        given(q.setParameter(anyString(), any())).willReturn(q);
        given(q.setMaxResults(anyInt())).willReturn(q);
        given(q.getResultList()).willReturn(List.of());
        given(em.createQuery(anyString(), eq(Tuple.class))).willReturn(q);

        List<DashboardStats.ErrorCountByCode> result = sut.findTopErrorCodes(OffsetDateTime.now(), 5);

        assertThat(result).isEmpty();
    }
}
