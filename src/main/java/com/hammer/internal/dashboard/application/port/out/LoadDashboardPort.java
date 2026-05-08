package com.hammer.internal.dashboard.application.port.out;

import com.hammer.internal.dashboard.application.dto.DashboardStats;
import java.time.OffsetDateTime;
import java.util.List;

public interface LoadDashboardPort {

    long countUsers();

    long countUsersByStatus(short statusCode);

    long countQuizzes();

    long countErrorLogsAfter(OffsetDateTime after);

    List<DashboardStats.ErrorCountByCode> findTopErrorCodes(OffsetDateTime after, int limit);

    List<DashboardStats.DailyErrorCount> findDailyErrorCounts(OffsetDateTime after);

    long countErrorLogs();

    long countNotificationTemplates();
}
