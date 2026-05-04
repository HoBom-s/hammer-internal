package com.hammer.internal.notification.adapter.out.persistence;

import com.hammer.internal.notification.domain.Channel;
import com.hammer.internal.notification.domain.NotificationTemplate;

final class NotificationTemplateMapper {

    private NotificationTemplateMapper() {}

    static NotificationTemplate toDomain(NotificationTemplateJpaEntity entity) {
        return new NotificationTemplate(
                entity.getId(),
                entity.getTemplateKey(),
                entity.getTitleTemplate(),
                entity.getBodyTemplate(),
                Channel.from(entity.getChannel()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    static NotificationTemplateJpaEntity toJpaEntity(NotificationTemplate template) {
        return new NotificationTemplateJpaEntity(
                template.getId(),
                template.getTemplateKey().value(),
                template.getTitleTemplate(),
                template.getBodyTemplate(),
                template.getChannel().name(),
                template.getCreatedAt(),
                template.getUpdatedAt());
    }
}
