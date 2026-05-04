package com.hammer.internal.notification.application.port.out;

import java.util.UUID;

public interface DeleteTemplatePort {

    void deleteById(UUID id);
}
