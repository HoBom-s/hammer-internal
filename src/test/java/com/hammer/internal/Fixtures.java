package com.hammer.internal;

import com.hammer.internal.notification.domain.Channel;
import com.hammer.internal.notification.domain.NotificationTemplate;
import com.hammer.internal.quiz.domain.Quiz;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Shared test fixtures. Provides canonical test objects to avoid
 * boilerplate construction across test classes.
 */
public final class Fixtures {

    private Fixtures() {}

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    public static User user() {
        return user(UUID.fromString("00000000-0000-0000-0000-000000000001"), UserStatus.Active);
    }

    public static User user(UUID id, UserStatus status) {
        return new User(id, "user@example.com", "testuser", status, null, "1.0", FIXED_TIME, FIXED_TIME);
    }

    public static Quiz quiz() {
        return quiz(1L);
    }

    public static Quiz quiz(Long id) {
        return new Quiz(id, "2+2는?", "3", "4", "5", "6", 1, "2+2=4 입니다", FIXED_TIME);
    }

    public static NotificationTemplate template() {
        return template(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    }

    public static NotificationTemplate template(UUID id) {
        return new NotificationTemplate(
                id, "welcome_push", "환영합니다", "{{name}}님 가입을 축하합니다", Channel.Push, FIXED_TIME, FIXED_TIME);
    }
}
