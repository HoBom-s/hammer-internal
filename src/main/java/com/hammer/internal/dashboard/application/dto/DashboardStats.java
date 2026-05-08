package com.hammer.internal.dashboard.application.dto;

import java.time.LocalDate;
import java.util.List;

public record DashboardStats(UserStats users, long totalQuizzes, ErrorLogStats errorLogs) {

    public record UserStats(long total, long active, long suspended, long deleted) {}

    public record ErrorLogStats(
            long totalToday, List<ErrorCountByCode> topErrorCodes, List<DailyErrorCount> dailyTrend) {}

    public record ErrorCountByCode(String errorCode, long count) {}

    public record DailyErrorCount(LocalDate date, long count) {}
}
