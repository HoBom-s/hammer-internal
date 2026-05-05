package com.hammer.internal.notification.application.port.out;

import com.hammer.internal.notification.domain.NotificationTemplate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadTemplatePort {

    Optional<NotificationTemplate> findById(UUID id);

    List<NotificationTemplate> findAllOrderByTemplateKey();

    boolean existsById(UUID id);

    boolean existsByTemplateKey(String templateKey);

    Optional<NotificationTemplate> findByTemplateKey(String templateKey);
}
