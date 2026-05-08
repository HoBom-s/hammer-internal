package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hammer.internal.Fixtures;
import com.hammer.internal.common.application.PagedResult;
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
    void returns_paged_templates_without_filters() {
        var t1 = Fixtures.template(UUID.randomUUID());
        given(loadTemplatePort.search(null, "", 1, 20)).willReturn(new PagedResult<>(List.of(t1), 1, 20, 1, 1));

        PagedResult<TemplateInfo> result = sut.listTemplates(1, 20, null, null);

        assertThat(result.items()).hasSize(1);
        assertThat(result.page()).isEqualTo(1);
    }

    @Test
    void returns_paged_templates_with_channel_filter() {
        var t1 = Fixtures.template(UUID.randomUUID());
        given(loadTemplatePort.search("Push", "", 1, 20)).willReturn(new PagedResult<>(List.of(t1), 1, 20, 1, 1));

        PagedResult<TemplateInfo> result = sut.listTemplates(1, 20, "Push", null);

        assertThat(result.items()).hasSize(1);
    }

    @Test
    void returns_paged_templates_with_keyword() {
        var t1 = Fixtures.template(UUID.randomUUID());
        given(loadTemplatePort.search(null, "welcome", 1, 20)).willReturn(new PagedResult<>(List.of(t1), 1, 20, 1, 1));

        PagedResult<TemplateInfo> result = sut.listTemplates(1, 20, null, "welcome");

        assertThat(result.items()).hasSize(1);
    }

    @Test
    void returns_paged_templates_with_channel_and_keyword() {
        given(loadTemplatePort.search("InApp", "test", 1, 20)).willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

        PagedResult<TemplateInfo> result = sut.listTemplates(1, 20, "InApp", "test");

        assertThat(result.items()).isEmpty();
    }

    @Test
    void blank_filters_are_normalized() {
        given(loadTemplatePort.search(null, "", 1, 20)).willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

        PagedResult<TemplateInfo> result = sut.listTemplates(1, 20, "  ", "  ");

        assertThat(result.items()).isEmpty();
    }
}
