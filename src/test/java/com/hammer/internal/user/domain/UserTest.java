package com.hammer.internal.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hammer.internal.Fixtures;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void suspend_active_user_changes_status_and_updatedAt() {
        User user = Fixtures.user(ID, UserStatus.Active);
        var before = user.getUpdatedAt();

        user.suspend();

        assertThat(user.getStatus()).isEqualTo(UserStatus.Suspended);
        assertThat(user.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    void suspend_already_suspended_user_is_noop() {
        User user = Fixtures.user(ID, UserStatus.Suspended);
        var before = user.getUpdatedAt();

        user.suspend();

        assertThat(user.getStatus()).isEqualTo(UserStatus.Suspended);
        assertThat(user.getUpdatedAt()).isEqualTo(before);
    }

    @Test
    void suspend_deleted_user_throws() {
        User user = Fixtures.user(ID, UserStatus.Deleted);

        assertThatThrownBy(user::suspend).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void activate_suspended_user_changes_status_and_updatedAt() {
        User user = Fixtures.user(ID, UserStatus.Suspended);
        var before = user.getUpdatedAt();

        user.activate();

        assertThat(user.getStatus()).isEqualTo(UserStatus.Active);
        assertThat(user.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    void activate_already_active_user_is_noop() {
        User user = Fixtures.user(ID, UserStatus.Active);
        var before = user.getUpdatedAt();

        user.activate();

        assertThat(user.getStatus()).isEqualTo(UserStatus.Active);
        assertThat(user.getUpdatedAt()).isEqualTo(before);
    }

    @Test
    void activate_deleted_user_throws() {
        User user = Fixtures.user(ID, UserStatus.Deleted);

        assertThatThrownBy(user::activate).isInstanceOf(IllegalArgumentException.class);
    }
}
