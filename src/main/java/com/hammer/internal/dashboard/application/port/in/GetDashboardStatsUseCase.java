package com.hammer.internal.dashboard.application.port.in;

import com.hammer.internal.dashboard.application.dto.DashboardStats;

public interface GetDashboardStatsUseCase {

    DashboardStats getStats(int trendDays);
}
