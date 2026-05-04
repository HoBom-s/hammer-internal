package com.hammer.internal.user;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String nickname,
        String status,
        String agreedTermsVersion,
        OffsetDateTime createdAt,
        OffsetDateTime deletedAt
) {

    static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getStatusName(),
                user.getAgreedTermsVersion(),
                user.getCreatedAt(),
                user.getDeletedAt());
    }
}
