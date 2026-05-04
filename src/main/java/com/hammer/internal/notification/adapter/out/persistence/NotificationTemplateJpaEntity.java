package com.hammer.internal.notification.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_templates", schema = "auction")
class NotificationTemplateJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "template_key", nullable = false, unique = true, length = 128)
    private String templateKey;

    @Column(name = "title_template", nullable = false, length = 512)
    private String titleTemplate;

    @Column(name = "body_template", nullable = false, length = 2048)
    private String bodyTemplate;

    @Column(nullable = false, length = 16)
    private String channel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected NotificationTemplateJpaEntity() {}

    NotificationTemplateJpaEntity(
            UUID id,
            String templateKey,
            String titleTemplate,
            String bodyTemplate,
            String channel,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.templateKey = templateKey;
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.channel = channel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    UUID getId() {
        return id;
    }

    String getTemplateKey() {
        return templateKey;
    }

    String getTitleTemplate() {
        return titleTemplate;
    }

    String getBodyTemplate() {
        return bodyTemplate;
    }

    String getChannel() {
        return channel;
    }

    OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
