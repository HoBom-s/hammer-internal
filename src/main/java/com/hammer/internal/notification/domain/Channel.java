package com.hammer.internal.notification.domain;

public enum Channel {
    Push,
    InApp,
    Both;

    public static Channel from(String value) {
        return valueOf(value);
    }
}
