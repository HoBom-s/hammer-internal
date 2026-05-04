package com.hammer.internal.notification.adapter.in.web;

import com.hammer.internal.notification.application.dto.CreateTemplateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "알림 템플릿 생성 요청")
record CreateNotificationTemplateRequest(
        @Schema(description = "템플릿 키 (snake_case, 고유값)", example = "welcome_push") @NotBlank @Size(max = 128) String templateKey,
        @Schema(description = "알림 제목 템플릿", example = "환영합니다") @NotBlank @Size(max = 512) String titleTemplate,
        @Schema(description = "알림 본문 템플릿 (변수: {name}, {date} 등)", example = "{{name}}님 가입을 축하합니다")
                @NotBlank @Size(max = 2048) String bodyTemplate,
        @Schema(
                        description = "발송 채널",
                        example = "Push",
                        allowableValues = {"Push", "InApp", "Both"})
                @NotBlank @Pattern(regexp = "Push|InApp|Both") String channel) {

    CreateTemplateCommand toCommand() {
        return new CreateTemplateCommand(templateKey, titleTemplate, bodyTemplate, channel);
    }
}
