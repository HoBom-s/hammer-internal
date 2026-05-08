package com.hammer.internal.errorlog.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;
import com.hammer.internal.errorlog.application.dto.ErrorLogSearchCriteria;
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
    ListErrorLogsService sut;

    @Test
    void returns_all_error_logs_without_criteria() {
        var criteria = new ErrorLogSearchCriteria(null, null, null, null, null);
        var errorLog = Fixtures.errorLog();
        given(loadErrorLogPort.search(criteria, 1, 20)).willReturn(new PagedResult<>(List.of(errorLog), 1, 20, 1, 1));

        PagedResult<ErrorLogInfo> result = sut.listErrorLogs(1, 20, criteria);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).status()).isEqualTo(500);
    }

    @Test
    void returns_error_logs_filtered_by_status() {
        var criteria = new ErrorLogSearchCriteria(400, null, null, null, null);
        var errorLog = new ErrorLog(2L, "POST", "/api", 400, "BAD_REQUEST", "bad", "trace", "{}", OffsetDateTime.now());
        given(loadErrorLogPort.search(criteria, 1, 10)).willReturn(new PagedResult<>(List.of(errorLog), 1, 10, 1, 1));

        PagedResult<ErrorLogInfo> result = sut.listErrorLogs(1, 10, criteria);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).errorCode()).isEqualTo("BAD_REQUEST");
    }

    @Test
    void returns_error_logs_filtered_by_error_code() {
        var criteria = new ErrorLogSearchCriteria(null, "NOT_FOUND", null, null, null);
        var errorLog = new ErrorLog(
                3L, "GET", "/api/users/1", 404, "NOT_FOUND", "not found", "stack", null, OffsetDateTime.now());
        given(loadErrorLogPort.search(criteria, 1, 20)).willReturn(new PagedResult<>(List.of(errorLog), 1, 20, 1, 1));

        PagedResult<ErrorLogInfo> result = sut.listErrorLogs(1, 20, criteria);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).uri()).isEqualTo("/api/users/1");
    }

    @Test
    void returns_empty_when_no_matches() {
        var criteria = new ErrorLogSearchCriteria(500, "INTERNAL_ERROR", null, null, "/nonexistent");
        given(loadErrorLogPort.search(criteria, 1, 20)).willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

        PagedResult<ErrorLogInfo> result = sut.listErrorLogs(1, 20, criteria);

        assertThat(result.items()).isEmpty();
    }
}
