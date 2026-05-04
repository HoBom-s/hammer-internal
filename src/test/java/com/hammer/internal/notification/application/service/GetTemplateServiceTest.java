package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.domain.NotificationTemplate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTemplateServiceTest {

    @Mock
    LoadTemplatePort loadTemplatePort;

    @InjectMocks
    GetTemplateService sut;

    @Test
    void returns_template_info_when_found() {
        UUID id = UUID.randomUUID();
        NotificationTemplate template = Fixtures.template(id);
        given(loadTemplatePort.findById(id)).willReturn(Optional.of(template));

        TemplateInfo result = sut.getTemplate(id);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.templateKey()).isEqualTo("welcome_push");
        assertThat(result.channel()).isEqualTo("Push");
    }

    @Test
    void throws_not_found_when_absent() {
        UUID id = UUID.randomUUID();
        given(loadTemplatePort.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getTemplate(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
