package com.hammer.internal.notification.application.dto;

import com.hammer.internal.notification.domain.NotificationTemplate;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "알림 템플릿 정보")
public record TemplateInfo(
        @Schema(description = "템플릿 ID", example = "550e8400-e29b-41d4-a716-446655440000") UUID id,
        @Schema(description = "템플릿 키", example = "welcome_push") String templateKey,
        @Schema(description = "제목 템플릿", example = "환영합니다") String titleTemplate,
        @Schema(description = "본문 템플릿", example = "{{name}}님 가입을 축하합니다") String bodyTemplate,
        @Schema(description = "발송 채널 (Push, InApp, Both)", example = "Push") String channel,
        @Schema(description = "생성 일시") OffsetDateTime createdAt,
        @Schema(description = "수정 일시") OffsetDateTime updatedAt) {

    public static TemplateInfo from(NotificationTemplate template) {
        return new TemplateInfo(
                template.getId(),
                template.getTemplateKey().value(),
                template.getTitleTemplate(),
                template.getBodyTemplate(),
                template.getChannel().name(),
                template.getCreatedAt(),
                template.getUpdatedAt());
    }
}
