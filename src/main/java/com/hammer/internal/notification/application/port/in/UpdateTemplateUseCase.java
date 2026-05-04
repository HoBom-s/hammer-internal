package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.dto.UpdateTemplateCommand;
import java.util.UUID;

public interface UpdateTemplateUseCase {

    TemplateInfo update(UUID id, UpdateTemplateCommand command);
}
