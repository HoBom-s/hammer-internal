package com.hammer.internal.notification.application.port.out;

import com.hammer.internal.notification.domain.NotificationTemplate;

public interface SaveTemplatePort {

    NotificationTemplate save(NotificationTemplate template);
}
