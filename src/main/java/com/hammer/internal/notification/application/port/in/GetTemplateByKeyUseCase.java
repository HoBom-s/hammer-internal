package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.notification.application.dto.TemplateInfo;

public interface GetTemplateByKeyUseCase {

    TemplateInfo getTemplateByKey(String templateKey);
}
