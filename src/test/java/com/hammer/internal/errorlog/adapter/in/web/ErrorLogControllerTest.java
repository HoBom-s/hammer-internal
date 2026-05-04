package com.hammer.internal.errorlog.adapter.in.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;
import com.hammer.internal.errorlog.application.port.in.ListErrorLogsUseCase;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ErrorLogController.class)
class ErrorLogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SaveErrorLogPort saveErrorLogPort;

    @MockitoBean
    ListErrorLogsUseCase listErrorLogsUseCase;

    @Test
    void returns_paged_error_logs_with_defaults() throws Exception {
        var log = new ErrorLogInfo(
                1L, "GET", "/test", 500, "INTERNAL_ERROR", "error", "stack", null, OffsetDateTime.now());
        given(listErrorLogsUseCase.listErrorLogs(1, 20, null)).willReturn(new PagedResult<>(List.of(log), 1, 20, 1, 1));

        mockMvc.perform(get("/internal/error-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].status").value(500))
                .andExpect(jsonPath("$.items[0].errorCode").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void applies_status_filter() throws Exception {
        given(listErrorLogsUseCase.listErrorLogs(2, 10, 400)).willReturn(new PagedResult<>(List.of(), 2, 10, 0, 0));

        mockMvc.perform(get("/internal/error-logs")
                        .param("page", "2")
                        .param("size", "10")
                        .param("status", "400"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.page").value(2));
    }
}
