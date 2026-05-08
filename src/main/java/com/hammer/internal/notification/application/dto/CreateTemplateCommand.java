package com.hammer.internal.notification.application.dto;

import com.hammer.internal.notification.domain.Channel;
import com.hammer.internal.notification.domain.NotificationTemplate;

public record CreateTemplateCommand(String templateKey, String titleTemplate, String bodyTemplate, String channel) {

    public NotificationTemplate toNotificationDomain() {
        return new NotificationTemplate(templateKey(), titleTemplate(), bodyTemplate(), Channel.from(channel()));
    }
}
