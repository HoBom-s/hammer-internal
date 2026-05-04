package com.hammer.internal.notification.application.dto;

import com.hammer.internal.notification.domain.NotificationTemplate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TemplateInfo(
        UUID id,
        String templateKey,
        String titleTemplate,
        String bodyTemplate,
        String channel,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static TemplateInfo from(NotificationTemplate template) {
        return new TemplateInfo(
                template.getId(),
                template.getTemplateKey().value(),
                template.getTitleTemplate(),
                template.getBodyTemplate(),
                template.getChannel().name(),
                template.getCreatedAt(),
                template.getUpdatedAt());
    }
}
