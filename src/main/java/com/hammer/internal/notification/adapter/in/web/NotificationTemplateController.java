package com.hammer.internal.notification.adapter.in.web;

import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.in.CreateTemplateUseCase;
import com.hammer.internal.notification.application.port.in.DeleteTemplateUseCase;
import com.hammer.internal.notification.application.port.in.GetTemplateUseCase;
import com.hammer.internal.notification.application.port.in.ListTemplatesUseCase;
import com.hammer.internal.notification.application.port.in.UpdateTemplateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Notification Templates", description = "알림 템플릿 관리 API")
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

    @Operation(summary = "알림 템플릿 전체 조회", description = "템플릿 키 기준 오름차순으로 전체 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public List<TemplateInfo> getAllTemplates() {
        return listTemplatesUseCase.listTemplates();
    }

    @Operation(summary = "알림 템플릿 단건 조회", description = "UUID로 특정 알림 템플릿을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "템플릿을 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public TemplateInfo getTemplate(
            @Parameter(description = "템플릿 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable
                    UUID id) {
        return getTemplateUseCase.getTemplate(id);
    }

    @Operation(summary = "알림 템플릿 생성", description = "새로운 알림 템플릿을 생성합니다. templateKey는 snake_case 형식이어야 하며 중복 불가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패 또는 templateKey 중복", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TemplateInfo createTemplate(@Valid @RequestBody CreateNotificationTemplateRequest request) {
        return createTemplateUseCase.create(request.toCommand());
    }

    @Operation(summary = "알림 템플릿 수정", description = "기존 알림 템플릿의 내용을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content),
        @ApiResponse(responseCode = "404", description = "템플릿을 찾을 수 없음", content = @Content)
    })
    @PutMapping("/{id}")
    public TemplateInfo updateTemplate(
            @Parameter(description = "템플릿 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id,
            @Valid @RequestBody UpdateNotificationTemplateRequest request) {
        return updateTemplateUseCase.update(id, request.toCommand());
    }

    @Operation(summary = "알림 템플릿 삭제", description = "알림 템플릿을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "템플릿을 찾을 수 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(
            @Parameter(description = "템플릿 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable
                    UUID id) {
        deleteTemplateUseCase.delete(id);
    }
}
