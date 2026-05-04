package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.hammer.internal.Fixtures;
import com.hammer.internal.notification.application.dto.CreateTemplateCommand;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.application.port.out.SaveTemplatePort;
import com.hammer.internal.notification.domain.NotificationTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTemplateServiceTest {

    @Mock
    LoadTemplatePort loadTemplatePort;

    @Mock
    SaveTemplatePort saveTemplatePort;

    @InjectMocks
    CreateTemplateService sut;

    @Test
    void saves_and_returns_new_template() {
        given(loadTemplatePort.existsByTemplateKey("new_key")).willReturn(false);
        NotificationTemplate saved = Fixtures.template();
        given(saveTemplatePort.save(any(NotificationTemplate.class))).willReturn(saved);

        var command = new CreateTemplateCommand("new_key", "제목", "본문", "Push");
        TemplateInfo result = sut.create(command);

        assertThat(result.templateKey()).isEqualTo("welcome_push");
        then(saveTemplatePort).should().save(any(NotificationTemplate.class));
    }

    @Test
    void throws_when_template_key_already_exists() {
        given(loadTemplatePort.existsByTemplateKey("duplicate_key")).willReturn(true);

        var command = new CreateTemplateCommand("duplicate_key", "t", "b", "Push");

        assertThatThrownBy(() -> sut.create(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duplicate_key");
        then(saveTemplatePort).should(never()).save(any());
    }
}
