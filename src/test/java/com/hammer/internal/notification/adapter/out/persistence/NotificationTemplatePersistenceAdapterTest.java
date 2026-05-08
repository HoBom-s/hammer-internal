package com.hammer.internal.notification.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.notification.domain.Channel;
import com.hammer.internal.notification.domain.NotificationTemplate;
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
class NotificationTemplatePersistenceAdapterTest {

    @Mock
    NotificationTemplateJpaRepository jpaRepository;

    @InjectMocks
    NotificationTemplatePersistenceAdapter sut;

    private static final OffsetDateTime FIXED = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    private static NotificationTemplateJpaEntity entity(UUID id) {
        return new NotificationTemplateJpaEntity(id, "welcome_push", "제목", "본문", "Push", FIXED, FIXED);
    }

    @Test
    void findById_returns_domain() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.findById(id)).willReturn(Optional.of(entity(id)));

        Optional<NotificationTemplate> result = sut.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getTemplateKey().value()).isEqualTo("welcome_push");
    }

    @Test
    void findById_returns_empty() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.findById(id)).willReturn(Optional.empty());

        assertThat(sut.findById(id)).isEmpty();
    }

    @Test
    void findAllOrderByTemplateKey_returns_list() {
        given(jpaRepository.findAllByOrderByTemplateKeyAsc()).willReturn(List.of(entity(UUID.randomUUID())));

        List<NotificationTemplate> result = sut.findAllOrderByTemplateKey();

        assertThat(result).hasSize(1);
    }

    @Test
    void existsById_delegates() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.existsById(id)).willReturn(true);

        assertThat(sut.existsById(id)).isTrue();
    }

    @Test
    void existsByTemplateKey_delegates() {
        given(jpaRepository.existsByTemplateKey("welcome_push")).willReturn(true);

        assertThat(sut.existsByTemplateKey("welcome_push")).isTrue();
    }

    @Test
    void findByTemplateKey_returns_domain() {
        given(jpaRepository.findByTemplateKey("welcome_push")).willReturn(Optional.of(entity(UUID.randomUUID())));

        Optional<NotificationTemplate> result = sut.findByTemplateKey("welcome_push");

        assertThat(result).isPresent();
    }

    @Test
    void search_returns_paged_result() {
        var page = new PageImpl<>(List.of(entity(UUID.randomUUID())), Pageable.ofSize(20), 1);
        given(jpaRepository.search(any(), any(), any(Pageable.class))).willReturn(page);

        PagedResult<NotificationTemplate> result = sut.search("Push", "welcome", 1, 20);

        assertThat(result.items()).hasSize(1);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    void search_without_filters() {
        var page =
                new PageImpl<>(List.of(entity(UUID.randomUUID()), entity(UUID.randomUUID())), Pageable.ofSize(20), 2);
        given(jpaRepository.search(any(), any(), any(Pageable.class))).willReturn(page);

        PagedResult<NotificationTemplate> result = sut.search(null, null, 1, 20);

        assertThat(result.items()).hasSize(2);
    }

    @Test
    void save_maps_and_persists() {
        UUID id = UUID.randomUUID();
        given(jpaRepository.save(any(NotificationTemplateJpaEntity.class))).willReturn(entity(id));

        NotificationTemplate template = new NotificationTemplate("welcome_push", "제목", "본문", Channel.Push);
        NotificationTemplate result = sut.save(template);

        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void deleteById_delegates() {
        UUID id = UUID.randomUUID();

        sut.deleteById(id);

        then(jpaRepository).should().deleteById(id);
    }
}
