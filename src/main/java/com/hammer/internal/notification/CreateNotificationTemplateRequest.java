package com.hammer.internal.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateNotificationTemplateRequest(
        @NotBlank @Size(max = 128) String templateKey,
        @NotBlank @Size(max = 512) String titleTemplate,
        @NotBlank @Size(max = 2048) String bodyTemplate,
        @NotBlank @Pattern(regexp = "Push|InApp|Both") String channel
) {
}
