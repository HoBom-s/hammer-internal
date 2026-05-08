package com.hammer.internal.dashboard.application.service;

import com.hammer.internal.dashboard.application.dto.OverviewCounts;
import com.hammer.internal.dashboard.application.port.in.GetOverviewCountsUseCase;
import com.hammer.internal.dashboard.application.port.out.LoadDashboardPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetOverviewCountsService implements GetOverviewCountsUseCase {

    private final LoadDashboardPort loadDashboardPort;

    GetOverviewCountsService(LoadDashboardPort loadDashboardPort) {
        this.loadDashboardPort = loadDashboardPort;
    }

    @Override
    public OverviewCounts getCounts() {
        return new OverviewCounts(
                loadDashboardPort.countUsers(),
                loadDashboardPort.countQuizzes(),
                loadDashboardPort.countErrorLogs(),
                loadDashboardPort.countNotificationTemplates());
    }
}
