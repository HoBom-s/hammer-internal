package com.hammer.internal.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {

    List<NotificationTemplate> findAllByOrderByTemplateKeyAsc();

    Optional<NotificationTemplate> findByTemplateKey(String templateKey);

    boolean existsByTemplateKey(String templateKey);
}
