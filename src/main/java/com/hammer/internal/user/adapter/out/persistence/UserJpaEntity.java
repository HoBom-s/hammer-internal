package com.hammer.internal.user.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "hammer")
class UserJpaEntity {

    @Id
    private UUID id;

    @Column(length = 256)
    private String email;

    @Column(nullable = false, length = 256)
    private String nickname;

    @Column(nullable = false)
    private short status;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "agreed_terms_version", length = 20)
    private String agreedTermsVersion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected UserJpaEntity() {}

    UserJpaEntity(
            UUID id,
            String email,
            String nickname,
            short status,
            OffsetDateTime deletedAt,
            String agreedTermsVersion,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.status = status;
        this.deletedAt = deletedAt;
        this.agreedTermsVersion = agreedTermsVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    UUID getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getNickname() {
        return nickname;
    }

    short getStatus() {
        return status;
    }

    OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    String getAgreedTermsVersion() {
        return agreedTermsVersion;
    }

    OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
