package com.hammer.internal.errorlog.adapter.out.persistence;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.errorlog.application.dto.ErrorLogSearchCriteria;
import com.hammer.internal.errorlog.application.port.out.LoadErrorLogPort;
import com.hammer.internal.errorlog.domain.ErrorLog;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public PagedResult<ErrorLog> search(ErrorLogSearchCriteria criteria, int page, int size) {
        Specification<ErrorLogJpaEntity> spec = buildSpecification(criteria);
        Page<ErrorLogJpaEntity> result =
                jpaRepository.findAll(spec, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return new PagedResult<>(
                result.getContent().stream().map(ErrorLogMapper::toDomain).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }

    private Specification<ErrorLogJpaEntity> buildSpecification(ErrorLogSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.status() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.status()));
            }
            if (criteria.errorCode() != null && !criteria.errorCode().isBlank()) {
                predicates.add(
                        cb.equal(root.get("errorCode"), criteria.errorCode().strip()));
            }
            if (criteria.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.from()));
            }
            if (criteria.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.to()));
            }
            if (criteria.uri() != null && !criteria.uri().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("uri")), "%" + criteria.uri().strip().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
