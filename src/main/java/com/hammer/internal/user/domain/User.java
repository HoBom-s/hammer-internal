package com.hammer.internal.user.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Email email;
    private final Nickname nickname;
    private final UserStatus status;
    private final OffsetDateTime deletedAt;
    private final String agreedTermsVersion;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public User(
            UUID id,
            String email,
            String nickname,
            UserStatus status,
            OffsetDateTime deletedAt,
            String agreedTermsVersion,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.email = new Email(email);
        this.nickname = new Nickname(nickname);
        this.status = status;
        this.deletedAt = deletedAt;
        this.agreedTermsVersion = agreedTermsVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public UserStatus getStatus() {
        return status;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public String getAgreedTermsVersion() {
        return agreedTermsVersion;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
