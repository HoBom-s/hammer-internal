package com.hammer.internal.notification.application.service;

import com.hammer.internal.notification.application.dto.CreateTemplateCommand;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.CreateTemplateUseCase;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.application.port.out.SaveTemplatePort;
import com.hammer.internal.notification.domain.Channel;
import com.hammer.internal.notification.domain.NotificationTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class CreateTemplateService implements CreateTemplateUseCase {

    private final LoadTemplatePort loadTemplatePort;
    private final SaveTemplatePort saveTemplatePort;

    CreateTemplateService(LoadTemplatePort loadTemplatePort, SaveTemplatePort saveTemplatePort) {
        this.loadTemplatePort = loadTemplatePort;
        this.saveTemplatePort = saveTemplatePort;
    }

    @Override
    public TemplateInfo create(CreateTemplateCommand command) {
        if (loadTemplatePort.existsByTemplateKey(command.templateKey())) {
            throw new IllegalArgumentException("Template key already exists: " + command.templateKey());
        }
        NotificationTemplate template = new NotificationTemplate(
                command.templateKey(),
                command.titleTemplate(),
                command.bodyTemplate(),
                Channel.from(command.channel()));
        NotificationTemplate saved = saveTemplatePort.save(template);
        return TemplateInfo.from(saved);
    }
}
