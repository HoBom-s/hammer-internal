package com.hammer.internal.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListUsersServiceTest {

    @Mock
    LoadUserPort loadUserPort;

    @InjectMocks
    ListUsersService sut;

    @Test
    void delegates_to_findAll_when_status_is_null() {
        User user = Fixtures.user();
        given(loadUserPort.findAll(1, 20)).willReturn(new PagedResult<>(List.of(user), 1, 20, 1, 1));

        PagedResult<UserInfo> result = sut.listUsers(1, 20, null);

        assertThat(result.items()).hasSize(1);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    void delegates_to_findByStatus_when_status_provided() {
        User user = Fixtures.user(UUID.randomUUID(), UserStatus.Suspended);
        given(loadUserPort.findByStatus(UserStatus.Suspended, 1, 10))
                .willReturn(new PagedResult<>(List.of(user), 1, 10, 1, 1));

        PagedResult<UserInfo> result = sut.listUsers(1, 10, UserStatus.Suspended);

        assertThat(result.items()).singleElement().satisfies(info -> {
            assertThat(info.status()).isEqualTo("Suspended");
        });
    }

    @Test
    void returns_empty_page_when_no_users() {
        given(loadUserPort.findAll(1, 20)).willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

        PagedResult<UserInfo> result = sut.listUsers(1, 20, null);

        assertThat(result.items()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }
}
