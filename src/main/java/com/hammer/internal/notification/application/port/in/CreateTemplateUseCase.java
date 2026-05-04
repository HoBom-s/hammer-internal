package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.notification.application.dto.CreateTemplateCommand;
import com.hammer.internal.notification.application.dto.TemplateInfo;

public interface CreateTemplateUseCase {

    TemplateInfo create(CreateTemplateCommand command);
}
