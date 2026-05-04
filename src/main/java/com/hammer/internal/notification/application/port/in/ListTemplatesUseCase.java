package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.notification.application.dto.TemplateInfo;
import java.util.List;

public interface ListTemplatesUseCase {

    List<TemplateInfo> listTemplates();
}
