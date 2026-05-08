package com.hammer.internal.dashboard.application.service;

import com.hammer.internal.dashboard.application.dto.DashboardStats;
import com.hammer.internal.dashboard.application.port.in.GetDashboardStatsUseCase;
import com.hammer.internal.dashboard.application.port.out.LoadDashboardPort;
import com.hammer.internal.user.domain.UserStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetDashboardStatsService implements GetDashboardStatsUseCase {

    private final LoadDashboardPort loadDashboardPort;

    GetDashboardStatsService(LoadDashboardPort loadDashboardPort) {
        this.loadDashboardPort = loadDashboardPort;
    }

    @Override
    public DashboardStats getStats(int trendDays) {
        OffsetDateTime todayStart = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime trendStart = todayStart.minusDays(trendDays);

        var userStats = new DashboardStats.UserStats(
                loadDashboardPort.countUsers(),
                loadDashboardPort.countUsersByStatus(UserStatus.Active.getCode()),
                loadDashboardPort.countUsersByStatus(UserStatus.Suspended.getCode()),
                loadDashboardPort.countUsersByStatus(UserStatus.Deleted.getCode()));

        long totalQuizzes = loadDashboardPort.countQuizzes();

        var errorLogStats = new DashboardStats.ErrorLogStats(
                loadDashboardPort.countErrorLogsAfter(todayStart),
                loadDashboardPort.findTopErrorCodes(trendStart, 5),
                loadDashboardPort.findDailyErrorCounts(trendStart));

        return new DashboardStats(userStats, totalQuizzes, errorLogStats);
    }
}
