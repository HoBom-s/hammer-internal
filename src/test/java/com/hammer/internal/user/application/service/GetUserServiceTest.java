package com.hammer.internal.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

    @Mock
    LoadUserPort loadUserPort;

    @InjectMocks
    GetUserService sut;

    @Test
    void returns_user_info_when_found() {
        UUID id = UUID.randomUUID();
        User user = Fixtures.user(id, UserStatus.Active);
        given(loadUserPort.findById(id)).willReturn(Optional.of(user));

        UserInfo result = sut.getUser(id);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.email()).isEqualTo("user@example.com");
        assertThat(result.status()).isEqualTo("Active");
    }

    @Test
    void throws_not_found_when_absent() {
        UUID id = UUID.randomUUID();
        given(loadUserPort.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getUser(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
