package com.hammer.internal.common.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.common.domain.NotFoundException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    SaveErrorLogPort saveErrorLogPort;

    GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(saveErrorLogPort);
    }

    @Test
    void handleNotFound_saves_error_log() {
        var request = new MockHttpServletRequest("GET", "/internal/users/123");
        var ex = new NotFoundException("User", "123");

        ProblemDetail result = handler.handleNotFound(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        then(saveErrorLogPort)
                .should()
                .save(
                        eq("GET"),
                        eq("/internal/users/123"),
                        eq(404),
                        eq("NOT_FOUND"),
                        anyString(),
                        anyString(),
                        isNull());
    }

    @Test
    void handleIllegalArgument_saves_error_log() {
        var request = new MockHttpServletRequest("POST", "/internal/quizzes");
        var ex = new IllegalArgumentException("invalid input");

        ProblemDetail result = handler.handleIllegalArgument(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(saveErrorLogPort)
                .should()
                .save(
                        eq("POST"),
                        eq("/internal/quizzes"),
                        eq(400),
                        eq("BAD_REQUEST"),
                        eq("invalid input"),
                        anyString(),
                        isNull());
    }

    @Test
    void handleUnexpected_saves_error_log() {
        var request = new MockHttpServletRequest("PUT", "/internal/error");
        var ex = new RuntimeException("unexpected");

        ProblemDetail result = handler.handleUnexpected(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getProperties()).containsEntry("code", "INTERNAL_ERROR");
        then(saveErrorLogPort)
                .should()
                .save(
                        eq("PUT"),
                        eq("/internal/error"),
                        eq(500),
                        eq("INTERNAL_ERROR"),
                        eq("unexpected"),
                        anyString(),
                        isNull());
    }

    @Test
    void extracts_request_body_from_caching_wrapper() {
        var request = new MockHttpServletRequest("POST", "/api");
        request.setContent("{\"key\":\"value\"}".getBytes(StandardCharsets.UTF_8));
        var wrapper = new ContentCachingRequestWrapper(request);
        // Force reading content so it's cached
        try {
            wrapper.getInputStream().readAllBytes();
        } catch (Exception ignored) {
        }

        var ex = new IllegalArgumentException("bad");
        handler.handleIllegalArgument(ex, wrapper);

        var bodyCaptor = ArgumentCaptor.forClass(String.class);
        then(saveErrorLogPort)
                .should()
                .save(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue()).isEqualTo("{\"key\":\"value\"}");
    }

    @Test
    void save_failure_does_not_propagate() {
        doThrow(new RuntimeException("db down"))
                .when(saveErrorLogPort)
                .save(any(), any(), anyInt(), any(), any(), any(), any());
        var request = new MockHttpServletRequest("GET", "/fail");
        var ex = new NotFoundException("X", "1");

        ProblemDetail result = handler.handleNotFound(ex, request);

        assertThat(result.getStatus()).isEqualTo(404);
    }
}
