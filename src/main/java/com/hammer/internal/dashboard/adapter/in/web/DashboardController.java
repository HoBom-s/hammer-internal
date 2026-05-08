package com.hammer.internal.dashboard.adapter.in.web;

import com.hammer.internal.dashboard.application.dto.DashboardStats;
import com.hammer.internal.dashboard.application.dto.OverviewCounts;
import com.hammer.internal.dashboard.application.port.in.GetDashboardStatsUseCase;
import com.hammer.internal.dashboard.application.port.in.GetOverviewCountsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "대시보드 통계 API")
@RestController
@RequestMapping("/internal/dashboard")
class DashboardController {

    private final GetDashboardStatsUseCase getDashboardStatsUseCase;
    private final GetOverviewCountsUseCase getOverviewCountsUseCase;

    DashboardController(
            GetDashboardStatsUseCase getDashboardStatsUseCase, GetOverviewCountsUseCase getOverviewCountsUseCase) {
        this.getDashboardStatsUseCase = getDashboardStatsUseCase;
        this.getOverviewCountsUseCase = getOverviewCountsUseCase;
    }

    @Operation(summary = "대시보드 통계 조회", description = "유저, 퀴즈, 에러 로그 통계를 한 번에 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/stats")
    public DashboardStats getStats(
            @Parameter(description = "에러 트렌드 조회 기간 (일)", example = "7") @RequestParam(defaultValue = "7")
                    int trendDays) {
        return getDashboardStatsUseCase.getStats(trendDays);
    }

    @Operation(summary = "전체 카운트 조회", description = "사용자, 퀴즈, 에러 로그, 알림 템플릿 전체 건수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/counts")
    public OverviewCounts getCounts() {
        return getOverviewCountsUseCase.getCounts();
    }
}
