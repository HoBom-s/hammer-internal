package com.hammer.internal.dashboard.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.dashboard.application.dto.DashboardStats;
import com.hammer.internal.dashboard.application.port.out.LoadDashboardPort;
import com.hammer.internal.user.domain.UserStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetDashboardStatsServiceTest {

    @Mock
    LoadDashboardPort loadDashboardPort;

    @InjectMocks
    GetDashboardStatsService sut;

    @Test
    void returns_aggregated_dashboard_stats() {
        given(loadDashboardPort.countUsers()).willReturn(100L);
        given(loadDashboardPort.countUsersByStatus(UserStatus.Active.getCode())).willReturn(80L);
        given(loadDashboardPort.countUsersByStatus(UserStatus.Suspended.getCode()))
                .willReturn(15L);
        given(loadDashboardPort.countUsersByStatus(UserStatus.Deleted.getCode()))
                .willReturn(5L);
        given(loadDashboardPort.countQuizzes()).willReturn(50L);
        given(loadDashboardPort.countErrorLogsAfter(any())).willReturn(10L);
        given(loadDashboardPort.findTopErrorCodes(any(), eq(5)))
                .willReturn(List.of(
                        new DashboardStats.ErrorCountByCode("NOT_FOUND", 5),
                        new DashboardStats.ErrorCountByCode("BAD_REQUEST", 3)));
        given(loadDashboardPort.findDailyErrorCounts(any()))
                .willReturn(List.of(new DashboardStats.DailyErrorCount(LocalDate.now(), 10)));

        DashboardStats result = sut.getStats(7);

        assertThat(result.users().total()).isEqualTo(100);
        assertThat(result.users().active()).isEqualTo(80);
        assertThat(result.users().suspended()).isEqualTo(15);
        assertThat(result.users().deleted()).isEqualTo(5);
        assertThat(result.totalQuizzes()).isEqualTo(50);
        assertThat(result.errorLogs().totalToday()).isEqualTo(10);
        assertThat(result.errorLogs().topErrorCodes()).hasSize(2);
        assertThat(result.errorLogs().dailyTrend()).hasSize(1);
    }

    @Test
    void returns_zero_stats_when_empty() {
        given(loadDashboardPort.countUsers()).willReturn(0L);
        given(loadDashboardPort.countUsersByStatus(UserStatus.Active.getCode())).willReturn(0L);
        given(loadDashboardPort.countUsersByStatus(UserStatus.Suspended.getCode()))
                .willReturn(0L);
        given(loadDashboardPort.countUsersByStatus(UserStatus.Deleted.getCode()))
                .willReturn(0L);
        given(loadDashboardPort.countQuizzes()).willReturn(0L);
        given(loadDashboardPort.countErrorLogsAfter(any())).willReturn(0L);
        given(loadDashboardPort.findTopErrorCodes(any(), anyInt())).willReturn(List.of());
        given(loadDashboardPort.findDailyErrorCounts(any())).willReturn(List.of());

        DashboardStats result = sut.getStats(7);

        assertThat(result.users().total()).isZero();
        assertThat(result.totalQuizzes()).isZero();
        assertThat(result.errorLogs().totalToday()).isZero();
        assertThat(result.errorLogs().topErrorCodes()).isEmpty();
        assertThat(result.errorLogs().dailyTrend()).isEmpty();
    }
}
