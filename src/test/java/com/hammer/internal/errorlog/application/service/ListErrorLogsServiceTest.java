package com.hammer.internal.errorlog.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;
import com.hammer.internal.errorlog.application.port.out.LoadErrorLogPort;
import com.hammer.internal.errorlog.domain.ErrorLog;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListErrorLogsServiceTest {

    @Mock
    LoadErrorLogPort loadErrorLogPort;

    @InjectMocks
    ListErrorLogsService listErrorLogsService;

    @Test
    void listErrorLogs_without_status_filter() {
        var errorLog =
                new ErrorLog(1L, "GET", "/test", 500, "INTERNAL_ERROR", "error", "stack", null, OffsetDateTime.now());
        given(loadErrorLogPort.findAll(1, 20)).willReturn(new PagedResult<>(List.of(errorLog), 1, 20, 1, 1));

        PagedResult<ErrorLogInfo> result = listErrorLogsService.listErrorLogs(1, 20, null);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).status()).isEqualTo(500);
        assertThat(result.items().get(0).errorCode()).isEqualTo("INTERNAL_ERROR");
    }

    @Test
    void listErrorLogs_with_status_filter() {
        var errorLog = new ErrorLog(2L, "POST", "/api", 400, "BAD_REQUEST", "bad", "trace", "{}", OffsetDateTime.now());
        given(loadErrorLogPort.findByStatus(400, 1, 10)).willReturn(new PagedResult<>(List.of(errorLog), 1, 10, 1, 1));

        PagedResult<ErrorLogInfo> result = listErrorLogsService.listErrorLogs(1, 10, 400);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).method()).isEqualTo("POST");
        assertThat(result.items().get(0).uri()).isEqualTo("/api");
    }
}
