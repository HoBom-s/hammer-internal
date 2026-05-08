package com.hammer.internal.errorlog.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface ErrorLogJpaRepository
        extends JpaRepository<ErrorLogJpaEntity, Long>, JpaSpecificationExecutor<ErrorLogJpaEntity> {}
