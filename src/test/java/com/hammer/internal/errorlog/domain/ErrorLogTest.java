package com.hammer.internal.errorlog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ErrorLogTest {

    @Test
    void create_factory_method_sets_fields() {
        ErrorLog errorLog = ErrorLog.create("POST", "/api/test", 500, "INTERNAL_ERROR", "msg", "trace", "{\"key\":1}");

        assertThat(errorLog.getId()).isNull();
        assertThat(errorLog.getMethod()).isEqualTo("POST");
        assertThat(errorLog.getUri()).isEqualTo("/api/test");
        assertThat(errorLog.getStatus()).isEqualTo(500);
        assertThat(errorLog.getErrorCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(errorLog.getMessage()).isEqualTo("msg");
        assertThat(errorLog.getStackTrace()).isEqualTo("trace");
        assertThat(errorLog.getRequestBody()).isEqualTo("{\"key\":1}");
        assertThat(errorLog.getCreatedAt()).isNull();
    }

    @Test
    void constructor_sets_all_fields() {
        java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
        ErrorLog errorLog = new ErrorLog(1L, "GET", "/uri", 404, "NOT_FOUND", "not found", null, null, now);

        assertThat(errorLog.getId()).isEqualTo(1L);
        assertThat(errorLog.getMethod()).isEqualTo("GET");
        assertThat(errorLog.getUri()).isEqualTo("/uri");
        assertThat(errorLog.getStatus()).isEqualTo(404);
        assertThat(errorLog.getErrorCode()).isEqualTo("NOT_FOUND");
        assertThat(errorLog.getMessage()).isEqualTo("not found");
        assertThat(errorLog.getStackTrace()).isNull();
        assertThat(errorLog.getRequestBody()).isNull();
        assertThat(errorLog.getCreatedAt()).isEqualTo(now);
    }
}
