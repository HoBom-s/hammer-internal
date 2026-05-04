package com.hammer.internal.notification.application.service;

import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.ListTemplatesUseCase;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import java.util.List;
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
    public List<TemplateInfo> listTemplates() {
        return loadTemplatePort.findAllOrderByTemplateKey().stream()
                .map(TemplateInfo::from)
                .toList();
    }
}
