package com.hammer.internal.notification.application.port.in;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.notification.application.dto.TemplateInfo;

public interface ListTemplatesUseCase {

    PagedResult<TemplateInfo> listTemplates(int page, int size, String channel, String keyword);
}
