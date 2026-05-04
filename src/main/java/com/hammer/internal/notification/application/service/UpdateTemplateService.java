package com.hammer.internal.notification.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.dto.UpdateTemplateCommand;
import com.hammer.internal.notification.application.port.in.UpdateTemplateUseCase;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.application.port.out.SaveTemplatePort;
import com.hammer.internal.notification.domain.Channel;
import com.hammer.internal.notification.domain.NotificationTemplate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class UpdateTemplateService implements UpdateTemplateUseCase {

    private final LoadTemplatePort loadTemplatePort;
    private final SaveTemplatePort saveTemplatePort;

    UpdateTemplateService(LoadTemplatePort loadTemplatePort, SaveTemplatePort saveTemplatePort) {
        this.loadTemplatePort = loadTemplatePort;
        this.saveTemplatePort = saveTemplatePort;
    }

    @Override
    public TemplateInfo update(UUID id, UpdateTemplateCommand command) {
        NotificationTemplate template =
                loadTemplatePort.findById(id).orElseThrow(() -> new NotFoundException("NotificationTemplate", id));
        template.update(
                command.templateKey(),
                command.titleTemplate(),
                command.bodyTemplate(),
                Channel.from(command.channel()));
        NotificationTemplate saved = saveTemplatePort.save(template);
        return TemplateInfo.from(saved);
    }
}
