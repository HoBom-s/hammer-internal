package com.hammer.internal.notification.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.GetTemplateUseCase;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetTemplateService implements GetTemplateUseCase {

    private final LoadTemplatePort loadTemplatePort;

    GetTemplateService(LoadTemplatePort loadTemplatePort) {
        this.loadTemplatePort = loadTemplatePort;
    }

    @Override
    public TemplateInfo getTemplate(UUID id) {
        return loadTemplatePort
                .findById(id)
                .map(TemplateInfo::from)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", id));
    }
}
