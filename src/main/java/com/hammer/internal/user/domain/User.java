package com.hammer.internal.user.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Email email;
    private final Nickname nickname;
    private UserStatus status;
    private final OffsetDateTime deletedAt;
    private final String agreedTermsVersion;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

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

    public void suspend() {
        if (status == UserStatus.Deleted) {
            throw new IllegalArgumentException("Deleted user cannot be suspended.");
        }
        if (status == UserStatus.Suspended) {
            return;
        }
        this.status = UserStatus.Suspended;
        this.updatedAt = OffsetDateTime.now();
    }

    public void activate() {
        if (status == UserStatus.Deleted) {
            throw new IllegalArgumentException("Deleted user cannot be activated.");
        }
        if (status == UserStatus.Active) {
            return;
        }
        this.status = UserStatus.Active;
        this.updatedAt = OffsetDateTime.now();
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
