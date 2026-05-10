package com.hammer.internal.user.adapter.out.persistence;

import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;

final class UserMapper {

    private UserMapper() {}

    static User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                UserStatus.fromCode(entity.getStatus()),
                entity.getDeletedAt(),
                entity.getAgreedTermsVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    static UserJpaEntity toJpaEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getEmail().value(),
                user.getNickname().value(),
                user.getStatus().getCode(),
                user.getDeletedAt(),
                user.getAgreedTermsVersion(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
