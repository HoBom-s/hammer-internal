package com.hammer.internal.notification.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.GetTemplateByKeyUseCase;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetTemplateByKeyService implements GetTemplateByKeyUseCase {

    private final LoadTemplatePort loadTemplatePort;

    GetTemplateByKeyService(LoadTemplatePort loadTemplatePort) {
        this.loadTemplatePort = loadTemplatePort;
    }

    @Override
    public TemplateInfo getTemplateByKey(String templateKey) {
        return loadTemplatePort
                .findByTemplateKey(templateKey)
                .map(TemplateInfo::from)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", templateKey));
    }
}
