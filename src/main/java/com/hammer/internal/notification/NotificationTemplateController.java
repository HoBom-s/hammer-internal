package com.hammer.internal.notification;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/notification-templates")
public class NotificationTemplateController {

    private final NotificationTemplateService templateService;

    public NotificationTemplateController(NotificationTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public List<NotificationTemplateResponse> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    @GetMapping("/{id}")
    public NotificationTemplateResponse getTemplate(@PathVariable UUID id) {
        return templateService.getTemplate(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationTemplateResponse createTemplate(
            @Valid @RequestBody CreateNotificationTemplateRequest request) {
        return templateService.createTemplate(request);
    }

    @PutMapping("/{id}")
    public NotificationTemplateResponse updateTemplate(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNotificationTemplateRequest request) {
        return templateService.updateTemplate(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
    }
}
