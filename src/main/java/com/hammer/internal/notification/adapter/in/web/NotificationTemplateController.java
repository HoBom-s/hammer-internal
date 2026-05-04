package com.hammer.internal.notification.adapter.in.web;

import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.CreateTemplateUseCase;
import com.hammer.internal.notification.application.port.in.DeleteTemplateUseCase;
import com.hammer.internal.notification.application.port.in.GetTemplateUseCase;
import com.hammer.internal.notification.application.port.in.ListTemplatesUseCase;
import com.hammer.internal.notification.application.port.in.UpdateTemplateUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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

@RestController
@RequestMapping("/internal/notification-templates")
class NotificationTemplateController {

    private final GetTemplateUseCase getTemplateUseCase;
    private final ListTemplatesUseCase listTemplatesUseCase;
    private final CreateTemplateUseCase createTemplateUseCase;
    private final UpdateTemplateUseCase updateTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;

    NotificationTemplateController(
            GetTemplateUseCase getTemplateUseCase,
            ListTemplatesUseCase listTemplatesUseCase,
            CreateTemplateUseCase createTemplateUseCase,
            UpdateTemplateUseCase updateTemplateUseCase,
            DeleteTemplateUseCase deleteTemplateUseCase) {
        this.getTemplateUseCase = getTemplateUseCase;
        this.listTemplatesUseCase = listTemplatesUseCase;
        this.createTemplateUseCase = createTemplateUseCase;
        this.updateTemplateUseCase = updateTemplateUseCase;
        this.deleteTemplateUseCase = deleteTemplateUseCase;
    }

    @GetMapping
    public List<TemplateInfo> getAllTemplates() {
        return listTemplatesUseCase.listTemplates();
    }

    @GetMapping("/{id}")
    public TemplateInfo getTemplate(@PathVariable UUID id) {
        return getTemplateUseCase.getTemplate(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TemplateInfo createTemplate(@Valid @RequestBody CreateNotificationTemplateRequest request) {
        return createTemplateUseCase.create(request.toCommand());
    }

    @PutMapping("/{id}")
    public TemplateInfo updateTemplate(
            @PathVariable UUID id, @Valid @RequestBody UpdateNotificationTemplateRequest request) {
        return updateTemplateUseCase.update(id, request.toCommand());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable UUID id) {
        deleteTemplateUseCase.delete(id);
    }
}
