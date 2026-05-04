package com.hammer.internal.user.application.dto;

import com.hammer.internal.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "사용자 정보")
public record UserInfo(
        @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000") UUID id,
        @Schema(description = "이메일", example = "user@example.com") String email,
        @Schema(description = "닉네임", example = "홍길동") String nickname,
        @Schema(description = "상태 (Active, Suspended, Deleted)", example = "Active") String status,
        @Schema(description = "동의한 약관 버전", example = "1.0") String agreedTermsVersion,
        @Schema(description = "가입 일시") OffsetDateTime createdAt,
        @Schema(description = "삭제 일시 (null이면 미삭제)") OffsetDateTime deletedAt) {

    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getEmail().value(),
                user.getNickname().value(),
                user.getStatus().name(),
                user.getAgreedTermsVersion(),
                user.getCreatedAt(),
                user.getDeletedAt());
    }
}
