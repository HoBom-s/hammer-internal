package com.hammer.internal.notification;

import com.hammer.internal.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;

    public NotificationTemplateService(NotificationTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getAllTemplates() {
        return templateRepository.findAllByOrderByTemplateKeyAsc().stream()
                .map(NotificationTemplateResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplate(UUID id) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", id));
        return NotificationTemplateResponse.from(template);
    }

    @Transactional
    public NotificationTemplateResponse createTemplate(CreateNotificationTemplateRequest request) {
        if (templateRepository.existsByTemplateKey(request.templateKey())) {
            throw new IllegalArgumentException("Template key already exists: " + request.templateKey());
        }
        var template = new NotificationTemplate(
                request.templateKey(),
                request.titleTemplate(),
                request.bodyTemplate(),
                request.channel());
        NotificationTemplate saved = templateRepository.save(template);
        return NotificationTemplateResponse.from(saved);
    }

    @Transactional
    public NotificationTemplateResponse updateTemplate(UUID id, UpdateNotificationTemplateRequest request) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", id));
        template.update(
                request.templateKey(),
                request.titleTemplate(),
                request.bodyTemplate(),
                request.channel());
        return NotificationTemplateResponse.from(template);
    }

    @Transactional
    public void deleteTemplate(UUID id) {
        if (!templateRepository.existsById(id)) {
            throw new NotFoundException("NotificationTemplate", id);
        }
        templateRepository.deleteById(id);
    }
}
