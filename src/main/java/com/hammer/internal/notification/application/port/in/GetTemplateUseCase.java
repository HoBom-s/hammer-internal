package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.notification.application.dto.TemplateInfo;
import java.util.UUID;

public interface GetTemplateUseCase {

    TemplateInfo getTemplate(UUID id);
}
