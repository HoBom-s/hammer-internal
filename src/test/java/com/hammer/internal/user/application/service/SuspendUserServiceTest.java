package com.hammer.internal.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import com.hammer.internal.user.application.port.out.SaveUserPort;
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
class SuspendUserServiceTest {

    @Mock
    LoadUserPort loadUserPort;

    @Mock
    SaveUserPort saveUserPort;

    @InjectMocks
    SuspendUserService sut;

    @Test
    void loads_then_suspends_then_saves() {
        UUID id = UUID.randomUUID();
        User user = Fixtures.user(id, UserStatus.Active);
        given(loadUserPort.findById(id)).willReturn(Optional.of(user));
        given(saveUserPort.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        UserInfo result = sut.suspend(id);

        assertThat(result.status()).isEqualTo("Suspended");
    }

    @Test
    void throws_not_found_when_user_absent() {
        UUID id = UUID.randomUUID();
        given(loadUserPort.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.suspend(id)).isInstanceOf(NotFoundException.class);
        then(saveUserPort).should(never()).save(any());
    }

    @Test
    void throws_when_user_is_deleted() {
        UUID id = UUID.randomUUID();
        User user = Fixtures.user(id, UserStatus.Deleted);
        given(loadUserPort.findById(id)).willReturn(Optional.of(user));

        assertThatThrownBy(() -> sut.suspend(id)).isInstanceOf(IllegalArgumentException.class);
        then(saveUserPort).should(never()).save(any());
    }
}
