package com.hammer.internal.errorlog.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ErrorLogJpaRepository extends JpaRepository<ErrorLogJpaEntity, Long> {

    Page<ErrorLogJpaEntity> findByStatus(int status, Pageable pageable);
}
