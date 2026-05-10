package com.hammer.internal.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock
    UserJpaRepository jpaRepository;

    @InjectMocks
    UserPersistenceAdapter sut;

    private static final OffsetDateTime FIXED = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    private static UserJpaEntity entity(UUID id, short status) {
        return new UserJpaEntity(id, "user@example.com", "tester", status, null, "1.0", FIXED, FIXED);
    }

    @Test
    void findById_returns_domain() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.findById(id)).willReturn(Optional.of(entity(id, (short) 1)));

        Optional<User> result = sut.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getStatus()).isEqualTo(UserStatus.Active);
    }

    @Test
    void findById_returns_empty() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.findById(id)).willReturn(Optional.empty());

        assertThat(sut.findById(id)).isEmpty();
    }

    @Test
    void findAll_returns_paged_result() {
        var page = new PageImpl<>(
                List.of(entity(UUID.randomUUID(), (short) 1), entity(UUID.randomUUID(), (short) 1)),
                Pageable.ofSize(20),
                2);
        given(jpaRepository.findAll(any(Pageable.class))).willReturn(page);

        PagedResult<User> result = sut.findAll(1, 20);

        assertThat(result.items()).hasSize(2);
        assertThat(result.page()).isEqualTo(1);
    }

    @Test
    void findByStatus_filters_by_status_code() {
        var page = new PageImpl<>(List.of(entity(UUID.randomUUID(), (short) 2)), Pageable.ofSize(20), 1);
        given(jpaRepository.findByStatus(any(Short.class), any(Pageable.class))).willReturn(page);

        PagedResult<User> result = sut.findByStatus(UserStatus.Suspended, 1, 20);

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).getStatus()).isEqualTo(UserStatus.Suspended);
    }

    @Test
    void save_persists_and_returns_domain() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.save(any(UserJpaEntity.class))).willReturn(entity(id, (short) 2));

        User user = new User(id, "user@example.com", "tester", UserStatus.Suspended, null, "1.0", FIXED, FIXED);
        User result = sut.save(user);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(UserStatus.Suspended);
    }
}
