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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTemplateByKeyServiceTest {

    @Mock
    LoadTemplatePort loadTemplatePort;

    @InjectMocks
    GetTemplateByKeyService sut;

    @Test
    void returns_template_when_found() {
        NotificationTemplate template = Fixtures.template();
        given(loadTemplatePort.findByTemplateKey("welcome_push")).willReturn(Optional.of(template));

        TemplateInfo result = sut.getTemplateByKey("welcome_push");

        assertThat(result.templateKey()).isEqualTo("welcome_push");
        assertThat(result.titleTemplate()).isEqualTo("환영합니다");
    }

    @Test
    void throws_not_found_when_absent() {
        given(loadTemplatePort.findByTemplateKey("unknown_key")).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getTemplateByKey("unknown_key"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("unknown_key");
    }
}
