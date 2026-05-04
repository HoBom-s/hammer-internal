package com.hammer.internal.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NotificationTemplateTest {

    @Nested
    class Creation {

        @Test
        void new_template_has_null_id_and_matching_timestamps() {
            NotificationTemplate template = new NotificationTemplate("welcome_push", "제목", "본문", Channel.Push);

            assertThat(template.getId()).isNull();
            assertThat(template.getTemplateKey()).isEqualTo(new TemplateKey("welcome_push"));
            assertThat(template.getChannel()).isEqualTo(Channel.Push);
            assertThat(template.getCreatedAt()).isEqualTo(template.getUpdatedAt());
        }

        @Test
        void reconstruction_preserves_all_fields() {
            UUID id = UUID.randomUUID();
            OffsetDateTime created = OffsetDateTime.parse("2024-01-01T00:00:00+09:00");
            OffsetDateTime updated = OffsetDateTime.parse("2024-06-01T00:00:00+09:00");

            NotificationTemplate template =
                    new NotificationTemplate(id, "order_confirm", "title", "body", Channel.InApp, created, updated);

            assertThat(template.getId()).isEqualTo(id);
            assertThat(template.getCreatedAt()).isEqualTo(created);
            assertThat(template.getUpdatedAt()).isEqualTo(updated);
        }
    }

    @Nested
    class Update {

        @Test
        void replaces_fields_and_advances_updatedAt() {
            OffsetDateTime originalTime = OffsetDateTime.parse("2024-01-01T00:00:00+09:00");
            NotificationTemplate template = new NotificationTemplate(
                    UUID.randomUUID(), "old_key", "t", "b", Channel.Push, originalTime, originalTime);

            template.update("new_key", "new-title", "new-body", Channel.Both);

            assertThat(template.getTemplateKey()).isEqualTo(new TemplateKey("new_key"));
            assertThat(template.getTitleTemplate()).isEqualTo("new-title");
            assertThat(template.getBodyTemplate()).isEqualTo("new-body");
            assertThat(template.getChannel()).isEqualTo(Channel.Both);
            assertThat(template.getUpdatedAt()).isAfter(originalTime);
        }

        @Test
        void does_not_affect_id_or_createdAt() {
            UUID id = UUID.randomUUID();
            OffsetDateTime created = OffsetDateTime.parse("2024-01-01T00:00:00+09:00");
            NotificationTemplate template =
                    new NotificationTemplate(id, "some_key", "t", "b", Channel.Push, created, created);

            template.update("updated_key", "new", "new", Channel.InApp);

            assertThat(template.getId()).isEqualTo(id);
            assertThat(template.getCreatedAt()).isEqualTo(created);
        }
    }

    @Nested
    class ChannelEnum {

        @ParameterizedTest(name = "\"{0}\" → valid Channel")
        @ValueSource(strings = {"Push", "InApp", "Both"})
        void from_parses_valid_values(String value) {
            Channel channel = Channel.from(value);
            assertThat(channel.name()).isEqualTo(value);
        }

        @ParameterizedTest(name = "\"{0}\" → IllegalArgumentException")
        @ValueSource(strings = {"push", "PUSH", "email", ""})
        void from_throws_on_invalid_values(String invalid) {
            assertThatThrownBy(() -> Channel.from(invalid)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
