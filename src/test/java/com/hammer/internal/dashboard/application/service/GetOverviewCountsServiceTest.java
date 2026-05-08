package com.hammer.internal.dashboard.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.dashboard.application.dto.OverviewCounts;
import com.hammer.internal.dashboard.application.port.out.LoadDashboardPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetOverviewCountsServiceTest {

    @Mock
    LoadDashboardPort loadDashboardPort;

    @InjectMocks
    GetOverviewCountsService sut;

    @Test
    void returns_all_counts() {
        given(loadDashboardPort.countUsers()).willReturn(100L);
        given(loadDashboardPort.countQuizzes()).willReturn(50L);
        given(loadDashboardPort.countErrorLogs()).willReturn(200L);
        given(loadDashboardPort.countNotificationTemplates()).willReturn(10L);

        OverviewCounts result = sut.getCounts();

        assertThat(result.users()).isEqualTo(100L);
        assertThat(result.quizzes()).isEqualTo(50L);
        assertThat(result.errorLogs()).isEqualTo(200L);
        assertThat(result.notificationTemplates()).isEqualTo(10L);
    }
}
