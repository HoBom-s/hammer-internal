package com.hammer.internal.common.application.port;

public interface SaveErrorLogPort {

    void save(
            String method,
            String uri,
            int status,
            String errorCode,
            String message,
            String stackTrace,
            String requestBody);
}
