package com.hammer.internal.errorlog.adapter.out.persistence;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.errorlog.application.port.out.LoadErrorLogPort;
import com.hammer.internal.errorlog.domain.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
class ErrorLogPersistenceAdapter implements SaveErrorLogPort, LoadErrorLogPort {

    private final ErrorLogJpaRepository jpaRepository;

    ErrorLogPersistenceAdapter(ErrorLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(
            String method,
            String uri,
            int status,
            String errorCode,
            String message,
            String stackTrace,
            String requestBody) {
        jpaRepository.save(new ErrorLogJpaEntity(method, uri, status, errorCode, message, stackTrace, requestBody));
    }

    @Override
    public PagedResult<ErrorLog> findAll(int page, int size) {
        Page<ErrorLogJpaEntity> result =
                jpaRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return toPagedResult(result, page, size);
    }

    @Override
    public PagedResult<ErrorLog> findByStatus(int status, int page, int size) {
        Page<ErrorLogJpaEntity> result = jpaRepository.findByStatus(
                status, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return toPagedResult(result, page, size);
    }

    private PagedResult<ErrorLog> toPagedResult(Page<ErrorLogJpaEntity> result, int page, int size) {
        return new PagedResult<>(
                result.getContent().stream().map(ErrorLogMapper::toDomain).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }
}
