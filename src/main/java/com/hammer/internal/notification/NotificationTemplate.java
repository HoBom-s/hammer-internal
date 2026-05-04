package com.hammer.internal.notification;

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
public class NotificationTemplate {

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

    protected NotificationTemplate() {
    }

    public NotificationTemplate(String templateKey, String titleTemplate,
                                String bodyTemplate, String channel) {
        this.templateKey = templateKey;
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.channel = channel;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() { return id; }

    public String getTemplateKey() { return templateKey; }

    public String getTitleTemplate() { return titleTemplate; }

    public String getBodyTemplate() { return bodyTemplate; }

    public String getChannel() { return channel; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void update(String templateKey, String titleTemplate,
                       String bodyTemplate, String channel) {
        this.templateKey = templateKey;
        this.titleTemplate = titleTemplate;
        this.bodyTemplate = bodyTemplate;
        this.channel = channel;
        this.updatedAt = OffsetDateTime.now();
    }
}
