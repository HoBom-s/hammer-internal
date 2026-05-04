package com.hammer.internal.user.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Page<UserJpaEntity> findByStatus(short status, Pageable pageable);
}
