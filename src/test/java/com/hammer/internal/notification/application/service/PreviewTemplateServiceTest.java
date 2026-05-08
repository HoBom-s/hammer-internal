package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hammer.internal.notification.application.dto.PreviewTemplateResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PreviewTemplateServiceTest {

    PreviewTemplateService sut = new PreviewTemplateService();

    @Test
    void replaces_variables_in_templates() {
        PreviewTemplateResponse result =
                sut.preview("안녕하세요 {{name}}님", "{{name}}님, {{event}} 알림입니다", Map.of("name", "홍길동", "event", "결제"));

        assertThat(result.renderedTitle()).isEqualTo("안녕하세요 홍길동님");
        assertThat(result.renderedBody()).isEqualTo("홍길동님, 결제 알림입니다");
    }

    @Test
    void preserves_unknown_variables() {
        PreviewTemplateResponse result = sut.preview("{{greeting}}", "{{name}}님 {{action}}", Map.of("name", "테스트"));

        assertThat(result.renderedTitle()).isEqualTo("{{greeting}}");
        assertThat(result.renderedBody()).isEqualTo("테스트님 {{action}}");
    }

    @Test
    void handles_empty_variables() {
        PreviewTemplateResponse result = sut.preview("{{title}}", "{{body}}", Map.of());

        assertThat(result.renderedTitle()).isEqualTo("{{title}}");
        assertThat(result.renderedBody()).isEqualTo("{{body}}");
    }

    @Test
    void handles_template_without_variables() {
        PreviewTemplateResponse result = sut.preview("고정 제목", "고정 본문", Map.of("unused", "value"));

        assertThat(result.renderedTitle()).isEqualTo("고정 제목");
        assertThat(result.renderedBody()).isEqualTo("고정 본문");
    }

    @Test
    void handles_multiple_occurrences_of_same_variable() {
        PreviewTemplateResponse result = sut.preview("{{name}} {{name}}", "{{name}}님", Map.of("name", "테스트"));

        assertThat(result.renderedTitle()).isEqualTo("테스트 테스트");
        assertThat(result.renderedBody()).isEqualTo("테스트님");
    }
}
