package com.hammer.internal.notification.application.service;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.ListTemplatesUseCase;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.domain.NotificationTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class ListTemplatesService implements ListTemplatesUseCase {

    private final LoadTemplatePort loadTemplatePort;

    ListTemplatesService(LoadTemplatePort loadTemplatePort) {
        this.loadTemplatePort = loadTemplatePort;
    }

    @Override
    public PagedResult<TemplateInfo> listTemplates(int page, int size, String channel, String keyword) {
        String normalizedChannel = (channel != null && !channel.isBlank()) ? channel.strip() : null;
        String normalizedKeyword = (keyword != null && !keyword.isBlank()) ? keyword.strip() : "";

        PagedResult<NotificationTemplate> result =
                loadTemplatePort.search(normalizedChannel, normalizedKeyword, page, size);
        return new PagedResult<>(
                result.items().stream().map(TemplateInfo::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages());
    }
}
