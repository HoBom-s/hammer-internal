package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListTemplatesServiceTest {

    @Mock
    LoadTemplatePort loadTemplatePort;

    @InjectMocks
    ListTemplatesService sut;

    @Test
    void returns_all_templates_ordered() {
        var t1 = Fixtures.template(UUID.randomUUID());
        var t2 = Fixtures.template(UUID.randomUUID());
        given(loadTemplatePort.findAllOrderByTemplateKey()).willReturn(List.of(t1, t2));

        List<TemplateInfo> result = sut.listTemplates();

        assertThat(result).hasSize(2);
    }

    @Test
    void returns_empty_list_when_no_templates() {
        given(loadTemplatePort.findAllOrderByTemplateKey()).willReturn(List.of());

        List<TemplateInfo> result = sut.listTemplates();

        assertThat(result).isEmpty();
    }
}
