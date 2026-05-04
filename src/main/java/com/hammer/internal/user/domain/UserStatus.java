package com.hammer.internal.user.domain;

public enum UserStatus {
    Active((short) 1),
    Suspended((short) 2),
    Deleted((short) 3);

    private final short code;

    UserStatus(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

    public static UserStatus fromCode(short code) {
        return switch (code) {
            case 1 -> Active;
            case 2 -> Suspended;
            case 3 -> Deleted;
            default -> throw new IllegalArgumentException("Unknown status code: " + code);
        };
    }
}
