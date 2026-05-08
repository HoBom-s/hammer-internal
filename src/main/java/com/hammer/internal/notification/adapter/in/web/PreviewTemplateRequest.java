package com.hammer.internal.notification.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

record PreviewTemplateRequest(
        @NotBlank @Size(max = 512) String titleTemplate,
        @NotBlank @Size(max = 2048) String bodyTemplate,
        @NotNull Map<String, String> variables) {}
