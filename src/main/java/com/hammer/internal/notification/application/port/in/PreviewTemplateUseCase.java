package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.notification.application.dto.PreviewTemplateResponse;
import java.util.Map;

public interface PreviewTemplateUseCase {

    PreviewTemplateResponse preview(String titleTemplate, String bodyTemplate, Map<String, String> variables);
}
