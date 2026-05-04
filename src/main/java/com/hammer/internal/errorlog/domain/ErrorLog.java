package com.hammer.internal.errorlog.domain;

import java.time.OffsetDateTime;

public class ErrorLog {

    private final Long id;
    private final String method;
    private final String uri;
    private final int status;
    private final String errorCode;
    private final String message;
    private final String stackTrace;
    private final String requestBody;
    private final OffsetDateTime createdAt;

    public ErrorLog(
            Long id,
            String method,
            String uri,
            int status,
            String errorCode,
            String message,
            String stackTrace,
            String requestBody,
            OffsetDateTime createdAt) {
        this.id = id;
        this.method = method;
        this.uri = uri;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.stackTrace = stackTrace;
        this.requestBody = requestBody;
        this.createdAt = createdAt;
    }

    public static ErrorLog create(
            String method,
            String uri,
            int status,
            String errorCode,
            String message,
            String stackTrace,
            String requestBody) {
        return new ErrorLog(null, method, uri, status, errorCode, message, stackTrace, requestBody, null);
    }

    public Long getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
