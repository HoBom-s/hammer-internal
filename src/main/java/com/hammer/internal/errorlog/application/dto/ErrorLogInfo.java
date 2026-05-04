package com.hammer.internal.errorlog.application.dto;

import com.hammer.internal.errorlog.domain.ErrorLog;
import java.time.OffsetDateTime;

public record ErrorLogInfo(
        Long id,
        String method,
        String uri,
        int status,
        String errorCode,
        String message,
        String stackTrace,
        String requestBody,
        OffsetDateTime createdAt) {

    public static ErrorLogInfo from(ErrorLog errorLog) {
        return new ErrorLogInfo(
                errorLog.getId(),
                errorLog.getMethod(),
                errorLog.getUri(),
                errorLog.getStatus(),
                errorLog.getErrorCode(),
                errorLog.getMessage(),
                errorLog.getStackTrace(),
                errorLog.getRequestBody(),
                errorLog.getCreatedAt());
    }
}
