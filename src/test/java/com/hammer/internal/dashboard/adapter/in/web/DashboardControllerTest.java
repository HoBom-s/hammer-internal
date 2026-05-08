package com.hammer.internal.dashboard.adapter.in.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.dashboard.application.dto.DashboardStats;
import com.hammer.internal.dashboard.application.dto.OverviewCounts;
import com.hammer.internal.dashboard.application.port.in.GetDashboardStatsUseCase;
import com.hammer.internal.dashboard.application.port.in.GetOverviewCountsUseCase;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SaveErrorLogPort saveErrorLogPort;

    @MockitoBean
    GetDashboardStatsUseCase getDashboardStatsUseCase;

    @MockitoBean
    GetOverviewCountsUseCase getOverviewCountsUseCase;

    @Test
    void returns_dashboard_stats_with_default_trend_days() throws Exception {
        var stats = new DashboardStats(
                new DashboardStats.UserStats(100, 80, 15, 5),
                50,
                new DashboardStats.ErrorLogStats(
                        10,
                        List.of(new DashboardStats.ErrorCountByCode("NOT_FOUND", 5)),
                        List.of(new DashboardStats.DailyErrorCount(LocalDate.now(), 10))));
        given(getDashboardStatsUseCase.getStats(7)).willReturn(stats);

        mockMvc.perform(get("/internal/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.total").value(100))
                .andExpect(jsonPath("$.users.active").value(80))
                .andExpect(jsonPath("$.users.suspended").value(15))
                .andExpect(jsonPath("$.users.deleted").value(5))
                .andExpect(jsonPath("$.totalQuizzes").value(50))
                .andExpect(jsonPath("$.errorLogs.totalToday").value(10))
                .andExpect(jsonPath("$.errorLogs.topErrorCodes[0].errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.errorLogs.dailyTrend").isArray());
    }

    @Test
    void respects_custom_trend_days() throws Exception {
        var stats = new DashboardStats(
                new DashboardStats.UserStats(0, 0, 0, 0), 0, new DashboardStats.ErrorLogStats(0, List.of(), List.of()));
        given(getDashboardStatsUseCase.getStats(30)).willReturn(stats);

        mockMvc.perform(get("/internal/dashboard/stats").param("trendDays", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalQuizzes").value(0));
    }

    @Test
    void returns_overview_counts() throws Exception {
        given(getOverviewCountsUseCase.getCounts()).willReturn(new OverviewCounts(100, 50, 200, 10));

        mockMvc.perform(get("/internal/dashboard/counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").value(100))
                .andExpect(jsonPath("$.quizzes").value(50))
                .andExpect(jsonPath("$.errorLogs").value(200))
                .andExpect(jsonPath("$.notificationTemplates").value(10));
    }
}
