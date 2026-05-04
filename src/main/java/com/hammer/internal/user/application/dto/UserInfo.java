package com.hammer.internal.user.application.dto;

import com.hammer.internal.user.domain.User;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String email,
        String nickname,
        String status,
        String agreedTermsVersion,
        OffsetDateTime createdAt,
        OffsetDateTime deletedAt) {

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
