package com.hammer.internal.errorlog.adapter.in.web;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;
import com.hammer.internal.errorlog.application.dto.ErrorLogSearchCriteria;
import com.hammer.internal.errorlog.application.port.in.ListErrorLogsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Error Logs", description = "에러 로그 조회 API")
@RestController
@RequestMapping("/internal/error-logs")
class ErrorLogController {

    private final ListErrorLogsUseCase listErrorLogsUseCase;

    ErrorLogController(ListErrorLogsUseCase listErrorLogsUseCase) {
        this.listErrorLogsUseCase = listErrorLogsUseCase;
    }

    @Operation(summary = "에러 로그 목록 조회", description = "페이징 및 다양한 필터를 적용하여 에러 로그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public PagedResult<ErrorLogInfo> getErrorLogs(
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "HTTP 상태 코드 필터 (예: 400, 500)") @RequestParam(required = false) Integer status,
            @Parameter(description = "에러 코드 필터 (예: NOT_FOUND, BAD_REQUEST)") @RequestParam(required = false)
                    String errorCode,
            @Parameter(description = "시작 일시 (ISO 8601)")
                    @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime from,
            @Parameter(description = "종료 일시 (ISO 8601)")
                    @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime to,
            @Parameter(description = "URI 패턴 검색") @RequestParam(required = false) String uri) {
        var criteria = new ErrorLogSearchCriteria(status, errorCode, from, to, uri);
        return listErrorLogsUseCase.listErrorLogs(page, size, criteria);
    }
}
