package com.hammer.internal.errorlog.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "error_logs", schema = "hammer")
class ErrorLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 2048)
    private String uri;

    @Column(nullable = false)
    private int status;

    @Column(name = "error_code", length = 64)
    private String errorCode;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected ErrorLogJpaEntity() {}

    ErrorLogJpaEntity(
            String method,
            String uri,
            int status,
            String errorCode,
            String message,
            String stackTrace,
            String requestBody) {
        this.method = method;
        this.uri = uri;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.stackTrace = stackTrace;
        this.requestBody = requestBody;
        this.createdAt = OffsetDateTime.now();
    }

    Long getId() {
        return id;
    }

    String getMethod() {
        return method;
    }

    String getUri() {
        return uri;
    }

    int getStatus() {
        return status;
    }

    String getErrorCode() {
        return errorCode;
    }

    String getMessage() {
        return message;
    }

    String getStackTrace() {
        return stackTrace;
    }

    String getRequestBody() {
        return requestBody;
    }

    OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
