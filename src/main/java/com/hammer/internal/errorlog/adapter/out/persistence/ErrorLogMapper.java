package com.hammer.internal.errorlog.adapter.out.persistence;

import com.hammer.internal.errorlog.domain.ErrorLog;

final class ErrorLogMapper {

    private ErrorLogMapper() {}

    static ErrorLog toDomain(ErrorLogJpaEntity entity) {
        return new ErrorLog(
                entity.getId(),
                entity.getMethod(),
                entity.getUri(),
                entity.getStatus(),
                entity.getErrorCode(),
                entity.getMessage(),
                entity.getStackTrace(),
                entity.getRequestBody(),
                entity.getCreatedAt());
    }
}
