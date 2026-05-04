package com.hammer.internal.notification.application.port.in;

import java.util.UUID;

public interface DeleteTemplateUseCase {

    void delete(UUID id);
}
