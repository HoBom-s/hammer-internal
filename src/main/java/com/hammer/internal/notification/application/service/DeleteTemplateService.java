package com.hammer.internal.notification.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.port.in.DeleteTemplateUseCase;
import com.hammer.internal.notification.application.port.out.DeleteTemplatePort;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class DeleteTemplateService implements DeleteTemplateUseCase {

    private final LoadTemplatePort loadTemplatePort;
    private final DeleteTemplatePort deleteTemplatePort;

    DeleteTemplateService(LoadTemplatePort loadTemplatePort, DeleteTemplatePort deleteTemplatePort) {
        this.loadTemplatePort = loadTemplatePort;
        this.deleteTemplatePort = deleteTemplatePort;
    }

    @Override
    public void delete(UUID id) {
        if (!loadTemplatePort.existsById(id)) {
            throw new NotFoundException("NotificationTemplate", id);
        }
        deleteTemplatePort.deleteById(id);
    }
}
