package com.hammer.internal.notification;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationTemplateResponse(
        UUID id,
        String templateKey,
        String titleTemplate,
        String bodyTemplate,
        String channel,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    static NotificationTemplateResponse from(NotificationTemplate template) {
        return new NotificationTemplateResponse(
                template.getId(),
                template.getTemplateKey(),
                template.getTitleTemplate(),
                template.getBodyTemplate(),
                template.getChannel(),
                template.getCreatedAt(),
                template.getUpdatedAt());
    }
}
