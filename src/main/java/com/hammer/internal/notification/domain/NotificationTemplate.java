package com.hammer.internal.notification.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class NotificationTemplate {

    private final UUID id;
    private TemplateKey templateKey;
    private String titleTemplate;
    private String bodyTemplate;
    private Channel channel;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public NotificationTemplate(String templateKey, String titleTemplate, String bodyTemplate, Channel channel) {
        this.id = null;
        this.templateKey = new TemplateKey(templateKey);
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.channel = channel;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public NotificationTemplate(
            UUID id,
            String templateKey,
            String titleTemplate,
            String bodyTemplate,
            Channel channel,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.templateKey = new TemplateKey(templateKey);
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.channel = channel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(String templateKey, String titleTemplate, String bodyTemplate, Channel channel) {
        this.templateKey = new TemplateKey(templateKey);
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.channel = channel;
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public TemplateKey getTemplateKey() {
        return templateKey;
    }

    public String getTitleTemplate() {
        return titleTemplate;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public Channel getChannel() {
        return channel;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
