package com.hammer.internal.notification.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateJpaEntity, UUID> {

    List<NotificationTemplateJpaEntity> findAllByOrderByTemplateKeyAsc();

    boolean existsByTemplateKey(String templateKey);

    Optional<NotificationTemplateJpaEntity> findByTemplateKey(String templateKey);
}
