package com.hammer.internal.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "hammer")
public class User {

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

    protected User() {
    }

    public UUID getId() { return id; }

    public String getEmail() { return email; }

    public String getNickname() { return nickname; }

    public short getStatus() { return status; }

    public OffsetDateTime getDeletedAt() { return deletedAt; }

    public String getAgreedTermsVersion() { return agreedTermsVersion; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public String getStatusName() {
        return switch (status) {
            case 1 -> "Active";
            case 2 -> "Suspended";
            case 3 -> "Deleted";
            default -> "Unknown";
        };
    }
}
