package com.hammer.internal.common.adapter.in.web;

import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.common.domain.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final SaveErrorLogPort saveErrorLogPort;

    public GlobalExceptionHandler(SaveErrorLogPort saveErrorLogPort) {
        this.saveErrorLogPort = saveErrorLogPort;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setProperty("code", "NOT_FOUND");
        saveError(request, HttpStatus.NOT_FOUND.value(), "NOT_FOUND", ex);
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setProperty("code", "BAD_REQUEST");
        saveError(request, HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", ex);
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setProperty("code", "VALIDATION_ERROR");
        problem.setProperty(
                "errors",
                ex.getFieldErrors().stream()
                        .map(e -> e.getField() + ": " + e.getDefaultMessage())
                        .toList());
        saveError(request, HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR", ex);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        problem.setProperty("code", "INTERNAL_ERROR");
        saveError(request, HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_ERROR", ex);
        return problem;
    }

    private void saveError(HttpServletRequest request, int status, String errorCode, Exception ex) {
        String body = extractRequestBody(request);
        String stackTrace = getStackTrace(ex);

        try {
            saveErrorLogPort.save(
                    request.getMethod(), request.getRequestURI(), status, errorCode, ex.getMessage(), stackTrace, body);
        } catch (Exception ignored) {
            // Prevent infinite loop if saving itself fails
        }
    }

    private String extractRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] content = wrapper.getContentAsByteArray();
            if (content.length > 0) {
                return new String(content, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
