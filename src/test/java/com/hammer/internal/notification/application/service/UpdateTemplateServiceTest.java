package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.dto.UpdateTemplateCommand;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.application.port.out.SaveTemplatePort;
import com.hammer.internal.notification.domain.NotificationTemplate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateTemplateServiceTest {

    @Mock
    LoadTemplatePort loadTemplatePort;

    @Mock
    SaveTemplatePort saveTemplatePort;

    @InjectMocks
    UpdateTemplateService sut;

    @Test
    void loads_then_updates_then_saves() {
        UUID id = UUID.randomUUID();
        NotificationTemplate existing = Fixtures.template(id);
        given(loadTemplatePort.findById(id)).willReturn(Optional.of(existing));
        given(saveTemplatePort.save(any(NotificationTemplate.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        var command = new UpdateTemplateCommand("updated_key", "새 제목", "새 본문", "Both");
        TemplateInfo result = sut.update(id, command);

        assertThat(result.templateKey()).isEqualTo("updated_key");
        assertThat(result.channel()).isEqualTo("Both");
    }

    @Test
    void throws_not_found_when_template_absent() {
        UUID id = UUID.randomUUID();
        given(loadTemplatePort.findById(id)).willReturn(Optional.empty());

        var command = new UpdateTemplateCommand("key", "t", "b", "Push");

        assertThatThrownBy(() -> sut.update(id, command)).isInstanceOf(NotFoundException.class);
        then(saveTemplatePort).should(never()).save(any());
    }
}
